package com.fs.dms;

import com.fs.dms.device.IDevice;
import com.fs.dms.rest.pojo.Device;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestPropertySource(locations ="classpath:application-test.properties")
@SpringBootTest(classes = DmsApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DmsApplicationTests {


	@LocalServerPort
	private int port;

	private TestRestTemplate restTemplate;

	private HttpHeaders headers;

	private List<String> deviceIds;

	static {
		System.setProperty("schemaFile", "C:\\Users\\smazumder\\IdeaProjects\\finalSubmission\\saumadip_mazumder\\src\\main\\resources\\deviceSchema.graphqls");
	}

	@Before
	public void setup()
	{
		deviceIds = new ArrayList<>();
		headers = new HttpHeaders();
		restTemplate = new TestRestTemplate();
	}


	@Test
	public void dmsIntegrationTest() throws JSONException {

		//1. Lets create a new device
		String deviceLocation = addDevice();

		//2. Lets find the created device
		getDeviceByID(deviceLocation);

		//3. If Device is founds, lets create another device and list all the the devices created so far.
		{
			addDevice();
			listDevices();
		}

		//4. Lets list the device by NEW type
		{
			JSONArray deviceByStatusArr = getDeviceByStatus(IDevice.Status.NEW);
			assertEquals(deviceIds.size(),deviceByStatusArr.length());
		}

		//5. As of now there should be no device by OK or STALE or UNHEALTHY type
		{

			assertEquals(0, getDeviceByStatus(IDevice.Status.OK).length());

			assertEquals(0,getDeviceByStatus(IDevice.Status.STALE).length());

			assertEquals(0,getDeviceByStatus(IDevice.Status.UNHEALTHY).length());

		}

		//6. Lets update the first device to OK
		updateDevice(IDevice.Status.OK,deviceIds.get(0));

		//7. Lets update the second device to OK
		updateDevice(IDevice.Status.OK,deviceIds.get(1));

		//7. There should be two devices in OK state
		assertEquals(2, getDeviceByStatus(IDevice.Status.OK).length());

		//9.Lets keep updating the second device OK, so that it doesn't get STALE after the given time
		keepPingingDevice(deviceIds.get(1));

		//8.Lets check if the OK device gets STALE after the given time
		try
		{
			Thread.sleep(6000L);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		assertEquals(1,getDeviceByStatus(IDevice.Status.STALE).length());
		assertEquals(1, getDeviceByStatus(IDevice.Status.OK).length());


	}

	private void keepPingingDevice(String id)
	{

		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

			scheduledExecutorService.scheduleAtFixedRate(() -> {
				try
				{
					updateDevice(IDevice.Status.OK,id);

				} catch (JSONException e)
				{
					throw new RuntimeException(e);
				}
			},1L,1L, TimeUnit.SECONDS);
	}


	private void getDeviceByID(String location) throws JSONException
	{
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(location, HttpMethod.GET, entity, String.class);

		String expectedDevice = "{\n" +
				"    \"deviceName\": \"Device1\",\n" +
				"    \"secretKey\": \"Secret1\",\n" +
				"    \"deviceStatus\": \"NEW\"\n" +
				"}";

		JSONAssert.assertEquals(expectedDevice,response.getBody(),true);

	}

	private JSONArray getDeviceByStatus(IDevice.Status status) throws JSONException
	{
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		String url = "/device/status/"+status.name();
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(url), HttpMethod.GET, entity, String.class);

		return new JSONArray(response.getBody());
	}


	private String addDevice() {

		Device device = new Device("Device1", "Secret1", IDevice.Status.NEW);

		HttpEntity<Device> entity = new HttpEntity<>(device, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/device/"),
				HttpMethod.POST, entity, String.class);

		String location = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

		String[] splitArr = location.split("/");

		String deviceId = splitArr[splitArr.length-1];

		assertTrue(deviceId.matches("^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}$"));

		deviceIds.add(deviceId);

		return location;

	}


	private void updateDevice(IDevice.Status status, String id) throws JSONException {

		Device device = new Device("Device1", "Secret1",status);

		HttpEntity<Device> entity = new HttpEntity<>(device, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/device/"+id), HttpMethod.PUT, entity, String.class);


		String expectedDevice = "{\n" +
				"    \"deviceID\":\""+ id + "\",\n"+
				"    \"deviceName\": \"Device1\",\n" +
				"    \"deviceStatus\": \"OK\"\n" +
				"}";

		JSONAssert.assertEquals(expectedDevice,response.getBody(),true);

	}

	public void listDevices() throws JSONException
	{

		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/device"), HttpMethod.GET, entity, String.class);

		JSONArray jsonArray = new JSONArray(response.getBody());

		assertEquals(deviceIds.size(),jsonArray.length());

	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}

