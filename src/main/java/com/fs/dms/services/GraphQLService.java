package com.fs.dms.services;

import com.fs.dms.device.IDevice;
import com.fs.dms.memoryservice.DeviceMemoryService;
import com.fs.dms.rest.pojo.Device;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GraphQLService
{


    @Autowired
    DeviceMemoryService deviceMemoryService;

    private GraphQL graphQL;


    @PostConstruct
    private void loadSchema() throws IOException
    {

        File schemafile = new File(System.getProperty("schemaFile"));

        TypeDefinitionRegistry registry = new SchemaParser().parse(schemafile);

        RuntimeWiring runtimeWiring = buildRunTimeWiring();

        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(registry,runtimeWiring);

        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }



    private RuntimeWiring buildRunTimeWiring()
    {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("alldevices", dataFeEnv -> deviceMemoryService.getDeviceMAp().values())
                        .dataFetcher("device", this::getDeviceBYID)
                        .dataFetcher("status", getDeviceByStatus())
                        ).build();
    }

    @NotNull
    private DataFetcher getDeviceByStatus()
    {
        return dataFeEnv-> deviceMemoryService.getDeviceMAp().values()
                .stream()
                .filter(device->device.getDeviceStatus().equals(IDevice.Status.valueOf(dataFeEnv.getArgument("deviceStatus"))))
                .collect(Collectors.toList());
    }

    @NotNull
    private Device getDeviceBYID(DataFetchingEnvironment dataFeEnv)
    {
        return new Device(deviceMemoryService.get(UUID.fromString(dataFeEnv.getArgument("deviceID"))));
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
