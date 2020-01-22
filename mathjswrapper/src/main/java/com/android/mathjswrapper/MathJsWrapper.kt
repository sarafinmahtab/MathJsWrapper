package com.android.mathjswrapper

import android.content.Context
import android.util.Log
import com.squareup.duktape.Duktape
import com.squareup.duktape.DuktapeException
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*

/**
 * A class that works as a wrapper of the mathjs.org (https://mathjs.org/) javascript library to evaluate math expressions.
 *
 * Uses overloaded [eval] method to evaluate expressions with variables scope.
 * Use [getExpressionNodes] method to get expression variables.
 * Use [onDestroy] to deallocate resources from memory.
 *
 * Created by sarafinmahtab on 1/22/20.
 */
class MathJsWrapper(context: Context) {

    companion object {
        private const val TAG = "MathJsWrapper"
    }

    private var duktape: Duktape?


    init {
        val stream = context.assets.open("math.min.js")
        val mathString = Scanner(stream, Charset.defaultCharset().name())
            .useDelimiter("\\A").next()
        duktape = Duktape.create()
        duktape?.evaluate(mathString)
    }


    /**
     * Executes prepared math eval script with [duktape] library.
     *
     * @param expression    expression to be evaluated
     * @return              final evaluated result as [String]
     */
    fun eval(expression: String): String? {
        if (duktape == null) {
            throw IllegalStateException("Can not evaluate after being destroyed")
        }

        try {
            val script = prepareEvalScript(expression)
            return duktape?.evaluate(script).toString()
        } catch (e: DuktapeException) {
            Log.e(TAG, "Duktape evaluation failed: ${e.message}")
        }
        return null
    }


    /**
     * Executes prepared math eval script with scope map with [duktape] library.
     *
     * @param expression    expression to be evaluated
     * @param scope         variables with value as [HashMap]
     * @return              final evaluated result as [String]
     */
    fun eval(expression: String, scope: HashMap<String, Any>?): String? {
        if (duktape == null) {
            throw IllegalStateException("Can not evaluate after being destroyed")
        }

        if (scope.isNullOrEmpty()) {
            throw NullPointerException("Scope can't be null or empty")
        }

        try {
            val script = prepareEvalScript(expression, scope)
            return duktape?.evaluate(script).toString()
        } catch (e: DuktapeException) {
            Log.e(TAG, "Duktape evaluation failed: ${e.message}")
        }
        return null
    }


    /**
     * Executes prepared math traverse script with type with [duktape] library.
     *
     * @param expression    expression to be evaluated
     * @param nodeType      specifying the type 'SymbolNode', 'OperatorNode', 'ConstantNode' or 'ParenthesisNode' as [NodeType]
     * @return              final list of variables with specified [nodeType] as [List]
     */
    fun getExpressionNodes(expression: String, nodeType: NodeType): List<String>? {
        if (duktape == null) {
            throw IllegalStateException("Can not evaluate after being destroyed")
        }

        return try {
            val script = prepareTraverseScript(expression, nodeType.type)
            val nodesResult = duktape?.evaluate(script).toString()

            nodesResult.split(",").dropLast(1)

        } catch (e: DuktapeException) {
            Log.e(TAG, "Duktape evaluation failed: ${e.message}")
            null
        }
    }


    /**
     * Prepare math eval expression using mathjs library.
     *
     * @param expression    expression to be evaluated
     * @return              prepared script as [String]
     */
    private fun prepareEvalScript(expression: String): String {
        return "(function() { return math.eval('$expression').toString(); })();"
    }


    /**
     * Prepare math eval expression with scope map using mathjs library.
     *
     * @param expression    expression to be evaluated
     * @param scope           variables with value as [HashMap]
     * @return              prepared script as [String]
     */
    private fun prepareEvalScript(expression: String, scope: HashMap<String, Any>): String {

        val scopeJson = JSONObject(scope.toString())

        return "(function() { " +
                "return math.eval('$expression', $scopeJson).toString(); })();"
    }


    /**
     * Prepare math expression variables by traversing it with type using mathjs library.
     *
     * @param expression    expression to be evaluated
     * @param nodeType      specifying the type 'SymbolNode', 'OperatorNode', 'ConstantNode' or 'ParenthesisNode'
     * @return              prepared script as [String]
     */
    private fun prepareTraverseScript(expression: String, nodeType: String): String {
        return "(function() { " +
                "var nodes = '';" +
                "math.parse('$expression').traverse(function (node, path, parent) {" +
                "if (node.type == '$nodeType') {" +
                "nodes = nodes.concat(node).concat(',')" +
                "}" +
                "});" +
                "return nodes;" +
                "})();"
    }


    /**
     * Deallocate [duktape] instance after being used.
     */
    fun onDestroy() {
        duktape?.close()
        duktape = null
    }
}
