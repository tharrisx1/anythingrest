package org.tharrisx.framework.rest.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.tharrisx.framework.pipe.exception.InvalidPipeDataException;
import org.tharrisx.framework.pipe.exception.PipeException;
import org.tharrisx.framework.store.exception.BeanStoreException;
import org.tharrisx.framework.store.exception.ConflictingBeansFoundException;
import org.tharrisx.framework.store.exception.NoBeanFoundException;
import org.tharrisx.framework.store.exception.NoSuchBeanPropertyException;
import org.tharrisx.framework.store.exception.OperationNotSupportedException;
import org.tharrisx.util.log.Log;

/**
 * Maps internal exceptions into standard JAX-RS exceptions handled in the RESTful way.
 * 
 * $$$ The ones at INFO logging should either be logged somewhere else, or, since they are user errors,
 * $$$ they perhaps should be stored for use in abuse blocking, to add some D-O-S attack protection.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class ExceptionMappers {

  @Provider
  public static class NoBeanFoundMapper extends UserErrorMapper<NoBeanFoundException> {
    @Override
    protected Status getResponseStatus() {
      return Response.Status.NOT_FOUND;
    }

    @Override
    protected String getMessage() {
      return "Store error: Requested item resource was not found.";
    }
  }

  @Provider
  public static class ConflictingBeansFoundMapper extends UserErrorMapper<ConflictingBeansFoundException> {
    @Override
    protected Status getResponseStatus() {
      return Response.Status.CONFLICT;
    }

    @Override
    protected String getMessage() {
      return "Store error: Requested item resource was found to be in conflict with other items.";
    }
  }

  @Provider
  public static class BeanTypeNotFoundMapper extends UserErrorMapper<ClassCastException> {
    @Override
    protected Status getResponseStatus() {
      return Response.Status.NOT_ACCEPTABLE;
    }

    @Override
    protected String getMessage() {
      return "User error: Requested type resource was not found.";
    }
  }

  @Provider
  public static class InvalidPipeDataMapper extends UserErrorMapper<InvalidPipeDataException> {
    @Override
    protected Status getResponseStatus() {
      return Response.Status.NOT_ACCEPTABLE;
    }

    @Override
    protected String getMessage() {
      return "User error: Supplied item data was not valid.";
    }
  }

  @Provider
  public static class NoSuchBeanPropertyMapper extends UserErrorMapper<NoSuchBeanPropertyException> {
    @Override
    protected Status getResponseStatus() {
      return Response.Status.NOT_ACCEPTABLE;
    }

    @Override
    protected String getMessage() {
      return "User error: Requested type property was not found.";
    }
  }

  @Provider
  public static class OperationNotSupportedMapper extends UserErrorMapper<OperationNotSupportedException> {
    @Override
    protected Status getResponseStatus() {
      return Response.Status.NOT_ACCEPTABLE;
    }

    @Override
    protected String getMessage() {
      return "User error: Requested operation is not supported.";
    }
  }

  @Provider
  public static class GeneralPipeMapper extends ServerErrorMapper<PipeException> {
    @Override
    protected String getMessage() {
      return "Server error: A general pipe exception has occurred. Contact your administrator.";
    }
  }

  @Provider
  public static class GeneralBeanStoreMapper extends ServerErrorMapper<BeanStoreException> {
    @Override
    protected String getMessage() {
      return "Server error: BeanStore is not working. Contact your administrator.";
    }
  }

  abstract static class UserErrorMapper<E extends Throwable> extends ExceptionCategoricalMapper<E> {
    @Override
    public final Response toResponse(E e) {
      // User error... Shouldn't treat as so important. Probably should log elsewhere... See above note.
      if(Log.isInfoEnabled(ExceptionMappers.class)) Log.info(ExceptionMappers.class, "UserErrorMapper", getMessage(), e);
      return makeResponse(getResponseStatus());
    }
  }

  abstract static class ServerErrorMapper<E extends Throwable> extends ExceptionCategoricalMapper<E> {
    @Override
    protected final Status getResponseStatus() {
      return Response.Status.INTERNAL_SERVER_ERROR;
    }

    @Override
    public final Response toResponse(E e) {
      // server error... Important.
      Log.error(ExceptionMappers.class, "ServerErrorMapper", getMessage(), e);
      return makeResponse(getResponseStatus());
    }
  }

  abstract static class ExceptionCategoricalMapper<E extends Throwable> implements ExceptionMapper<E> {
    protected abstract Response.Status getResponseStatus();

    protected abstract String getMessage();

    protected Response makeResponse(Response.Status status) {
      return Response.status(status).build();
    }
  }
}
