package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.TypeStrategy;
import org.springframework.stereotype.Component;

@Component
public class GlassfishAppCommandStrategy implements CommandStrategy{

    private String glassfish_user="usuario_glassfish";
    private String glassfish_password="contraseña_glassfish";
    private String glassfish_path="/ruta/del/glassfish";
    private String  app_name="nombre_de_la_aplicacion";
    private String  START="${glassfish_path}/bin/asadmin start-domain && ${glassfish_path}/bin/asadmin --user ${glassfish_user} --passwordfile ${glassfish_password} deploy --name ${app_name}";
    private String Stop ="asadmin --user=<username> --passwordfile=<ruta_al_archivo_de_contraseña> stop-app <nombre_de_la_aplicacion>";

    private String status ="asadmin --user=admin --passwordfile=/opt/glassfish3/glassfish/domains/domain1/config/passwords list-applications --type web | grep myapp";

    @Override
    public String start(final SshManagerDto sshManagerDto, final Aplication aplication) {
        return "";
    }

    @Override
    public String stop(final SshManagerDto sshManagerDto, final Aplication aplication) {
        return "";
    }

    @Override
    public boolean status(final SshManagerDto sshManagerDto, final Aplication aplication) {
        return true;
    }

    @Override
    public TypeStrategy getType() {
        return TypeStrategy.GLASSFISH_APP;
    }
}
