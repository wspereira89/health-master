package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.Server;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class SshManagerRepositoryImpl implements SshManagerRepository{
    private  List<Server> serverList ;

    public SshManagerRepositoryImpl() {
        Server server1 = new Server("1", "localhost", "root", "password");
        Server server2 = new Server("2", "localhost", "root", "password");
        Server server3 = new Server("3", "localhost", "root", "password");
       serverList= Arrays.asList(server1, server2, server3);
    }

    @Override
    public List<Server> getAllServer() {
        return serverList;
    }

    @Override
    public Optional<Server> getOne(String serverId) {
        return serverList.stream().filter(server -> server.getServerId().equals(serverId)).findFirst();
    }

    @Override
    public Server add(Server server) {
         serverList.add(server);
        return server;
    }

    @Override
    public void delete(String serverId) {
        serverList.removeIf( server->server.getServerId().equals(serverId));
    }
}
