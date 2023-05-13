package com.spc.healthmaster.ssh.repository;

import com.spc.healthmaster.ssh.entity.Server;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class SshManagerRepositoryImpl implements SshManagerRepository{
    @Override
    public List<Server> getAllServer() {
        Server server1 = new Server("1", "localhost", "root", "password");
        Server server2 = new Server("2", "localhost", "root", "password");
        Server server3 = new Server("3", "localhost", "root", "password");
        return Arrays.asList(server1, server2, server3);
    }
}
