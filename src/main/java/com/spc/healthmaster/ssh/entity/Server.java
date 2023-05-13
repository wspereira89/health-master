package com.spc.healthmaster.ssh.entity;


import com.spc.healthmaster.ssh.dto.SshManagerDto;

public final class Server {
    private final String serverId;
    private final String host;
    private final String user;
    private final String password;

    public Server(final String serverId, final String host, final String user, final String password) {
        this.serverId = serverId;
        this.host = host;
        this.user = user;
        this.password = password;
    }

    public String getServerId() {
        return serverId;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public SshManagerDto toSshManager() {
        return new SshManagerDto(this.host, this.user, this.password);
    }
}
