package com.example.lra;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.lra.LRAResponse;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

/**
 * Example resource with LRA.
 */
@Path("/lra-example")
@ApplicationScoped
public class LRAExampleResource {

    private static final Logger LOGGER = Logger.getLogger(LRAExampleResource.class.getName());

    @PUT
    @LRA(value = LRA.Type.REQUIRES_NEW, timeLimit = 500, timeUnit = ChronoUnit.MILLIS)
    @Path("start-example")
    public Response startExample(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId,
                                 String data) throws InterruptedException {

        if (data.contains("BOOM")) {
            throw new RuntimeException("BOOM 💥");
        }

        if (data.contains("TIMEOUT")) {
            Thread.sleep(2000);
        }

        LOGGER.info("Data " + data + " processed 🏭");
        return Response.ok().build();
    }

    @PUT
    @Complete
    @Path("complete-example")
    public Response completeExample(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        LOGGER.log(Level.INFO, "LRA id: {0} completed 🎉", lraId);
        return LRAResponse.completed();
    }

    @PUT
    @Compensate
    @Path("compensate-example")
    public Response compensateExample(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        LOGGER.log(Level.SEVERE, "LRA id: {0} compensated 🚒", lraId);
        return LRAResponse.compensated();
    }

}