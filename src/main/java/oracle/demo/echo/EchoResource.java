
package oracle.demo.echo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Path("/echo")
public class EchoResource{

    @Inject @ConfigProperty(name = "echo.reply", defaultValue = "Hi there!") 
    private String reply;

    @POST @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response echo(Message message, @Context UriInfo uriInfo) {

        final JsonObject json = Json.createObjectBuilder()
        .add("url", uriInfo.getRequestUri().toASCIIString())
        .add("text", message.text)
        .add("reply", reply)
        .build();
        return Response.ok(json.toString()).build();
    }

    public static class Message{
        public String text;
    }

    @GET @Path("/dump")
    @Produces(MediaType.APPLICATION_JSON)
    public Response dump(@Context UriInfo uriInfo, @Context HttpHeaders headers /*, @Context Request request*/) {

        final JsonObjectBuilder builder = Json.createObjectBuilder();
        headers.getRequestHeaders().forEach((s,l) -> {
            l.forEach(x -> {
                builder.add(s, x);
            });
        });
        JsonObject jsonHeaders = builder.build();

        final JsonObject response = Json.createObjectBuilder()
            .add("url", uriInfo.getRequestUri().toASCIIString())
            .add("headers", jsonHeaders)
            .build();
        return Response.ok(response.toString()).build();
    }


}