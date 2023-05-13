package com.spc.healthmaster.ssh.repository;

import com.spc.healthmaster.ssh.entity.Server;

import java.util.List;

public interface SshManagerRepository {

    List<Server> getAllServer();
}
