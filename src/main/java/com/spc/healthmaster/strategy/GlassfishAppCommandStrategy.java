package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlassfishAppCommandStrategy extends BaseCommandStrategy implements CommandStrategy {

    private String glassfish_user="usuario_glassfish";
    private String glassfish_password="contraseña_glassfish";
    private String glassfish_path="/ruta/del/glassfish";
    private String  app_name="nombre_de_la_aplicacion";
    private String  START="${glassfish_path}/bin/asadmin start-domain && ${glassfish_path}/bin/asadmin --user ${glassfish_user} --passwordfile ${glassfish_password} deploy --name ${app_name}";
    private String Stop ="asadmin --user=<username> --passwordfile=<ruta_al_archivo_de_contraseña> stop-app <nombre_de_la_aplicacion>";

    private String status ="asadmin --user=admin --passwordfile=/opt/glassfish3/glassfish/domains/domain1/config/passwords list-applications --type web | grep myapp";

    @Override
    public String start(final WrapperExecute wrapper) {
        return "";
    }

    @Override
    public String stop(final WrapperExecute wrapper) {
        return "";
    }

    @Override
    public boolean status(final WrapperExecute wrapper) {
        return true;
    }

    @Override
    public List<FileDto> getListFile(final WrapperExecute wrapper) throws ApiException {
        return super.getListFile(wrapper);
    }

    @Override
    public byte[] downloadFile(final WrapperExecute wrapper) throws ApiException {
        return super.downloadFile(wrapper);
    }

    @Override
    public TypeStrategy getType() {
        return TypeStrategy.GLASSFISH_APP;
    }
}
