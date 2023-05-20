package com.spc.healthmaster.dtos;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.spc.healthmaster.exception.ApiException;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.spc.healthmaster.factories.ApiErrorFactory.sshException;

@Data
public class SshManagerDto {
    /**
     * Constante que representa un enter.
     */
    private static final String ENTER_KEY = "n";
    private final String serverName;

    private final String host;
    private final String user;
    private final String password;
    private Session session;

    public SshManagerDto(final String serverName, final String host, final String user, final String password) {
        this.serverName = serverName;
        this.host = host;
        this.user = user;
        this.password = password;
    }

    private void connect() throws ApiException {

        if (this.session == null || !this.session.isConnected()) {
            final JSch jsch = new JSch();

            try {
                this.session = jsch.getSession(this.user, host, 2024);

            this.session.setPassword(password);

            // Parametro para no validar key de conexion.
            this.session.setConfig("StrictHostKeyChecking", "no");
            // Agregar el SessionListener

            this.session.connect();
            } catch (final JSchException e) {
                throw sshException(user, host).withCause("session", e.getMessage()).toException();
            }
        }
    }

   public void disconnect() {
        if (session != null) {
            session.disconnect();
        }
    }

    private boolean isConnected() {
        return this.session != null && !this.session.isConnected();
    }

    /**
     * Ejecuta un comando SSH.
     *
     * @param command Comando SSH a ejecutar.
     *
     * @return
     *
     * @throws IllegalAccessException Excepción lanzada cuando no hay
     *                                conexión establecida.
     * @throws JSchException          Excepción lanzada por algún
     *                                error en la ejecución del comando
     *                                SSH.
     * @throws IOException            Excepción al leer el texto arrojado
     *                                luego de la ejecución del comando
     *                                SSH.
     */
    public final String executeCommand(String command) throws ApiException {

        if (!this.isConnected()) {
            this.connect();
        }

        ChannelExec channelExec = null;
        try {
            channelExec = (ChannelExec) this.session.openChannel("exec");
            final InputStream in = channelExec.getInputStream();

            // Ejecutamos el comando.
            channelExec.setCommand(command);
            channelExec.connect();

            // Obtenemos el texto impreso en la consola.
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            final StringBuilder builder = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                builder.append(linea);
                builder.append(ENTER_KEY);
            }

            // Cerramos el canal SSH.
            channelExec.disconnect();

            // Retornamos el texto impreso en la consola.
            return builder.toString();
        } catch (final JSchException | IOException e) {
            throw sshException(user, host).withCause("exec", e.getMessage()).toException();
        }
    }
}
