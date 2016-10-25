package k.common

import jodd.exception.ExceptionUtil
import k.controllers.JsonpController
import k.reply.ReplyBase
import play.http.HttpErrorHandler
import play.mvc.Http
import play.mvc.Result
import play.mvc.Results
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

/**
 * Created by kk on 16/10/25.
 */
class ErrorHandler : HttpErrorHandler {

    override fun onClientError(request: Http.RequestHeader?, statusCode: Int, message: String?): CompletionStage<Result> {
        if (request!!.uri().startsWith("/api/")) {
            val reply = ReplyBase()
            reply.errmsg = message
            reply.ret = -1
            return CompletableFuture.completedFuture(JsonpController.ok(reply))
        } else {
            return CompletableFuture.completedFuture(Results.status(statusCode, "A client error occurred: " + message))
        }
    }

    override fun onServerError(request: Http.RequestHeader?, exception: Throwable?): CompletionStage<Result> {
        if (request!!.uri().startsWith("/api/")) {
            val reply = ReplyBase()
            reply.errmsg = ExceptionUtil.exceptionChainToString(exception)
            reply.ret = -1
            return CompletableFuture.completedFuture(JsonpController.ok(reply))
        } else {
            return CompletableFuture.completedFuture(Results.internalServerError("A server error occurred: " + exception!!.message))

        }
    }

}