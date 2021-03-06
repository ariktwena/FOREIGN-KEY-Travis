package rest;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    //HUSK CORS OG EXCEPTIONS I DETTE ARRAY
    private void addRestResourceClasses(Set<Class<?>> resources) {
        //HUSK CORS
        //resources.add(exceptions.GenericExceptionMapper.class); //DENNE SKAL MED MEN FORSVINDER
        resources.add(exceptions.GenericExceptionMapper.class);
        //resources.add(exceptions.PersonNotFoundExceptionMapper.class);
        resources.add(exceptions.PersonNotFoundExceptionMapper.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class); //ECEPTION MAPPER
        resources.add(rest.PersonResource.class);
        resources.add(rest.RenameMeResource.class);
    }
    
}
