package com.abrhernandez.meeting_golden.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class CustomGraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof CustomGraphQLException) {
            ErrorType errorType;
            if(((CustomGraphQLException) ex).getStatusCode()==400){
                errorType = ErrorType.BAD_REQUEST;
                return graphQLError(errorType, ex.getMessage(), env);
            }
            if(((CustomGraphQLException) ex).getStatusCode()==404){
                errorType = ErrorType.NOT_FOUND;
                return graphQLError(errorType, ex.getMessage(), env);
            }
            else {
                return GraphqlErrorBuilder.newError().build();
            }

        }else if(ex instanceof DataIntegrityViolationException){
            String message = "El campo: "+((PropertyValueException)ex.getCause()).getPropertyName()+" no puede estar vacio";
            return graphQLError(ErrorType.BAD_REQUEST, message, env);
        }else {
            return GraphqlErrorBuilder.newError().message(ex.getMessage()).build();
        }
    }

    private GraphQLError graphQLError(ErrorType errorType, String message,DataFetchingEnvironment env){
        return GraphqlErrorBuilder.newError()
                .errorType(errorType)
                .message(message)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}
