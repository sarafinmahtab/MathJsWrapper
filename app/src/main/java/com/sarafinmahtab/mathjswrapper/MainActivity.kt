package com.sarafinmahtab.mathjswrapper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.mathjswrapper.MathJsWrapper
import com.android.mathjswrapper.NodeType
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var mathJsWrapper: MathJsWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mathJsWrapper = MathJsWrapper(this)

        val defaultExpression = "( num1+ (string2* cos(float0) )-num1+sqrt(int0)/ num1)"
        evaluationEt.setText(defaultExpression)

        testButton.setOnClickListener {

            try {
                val map = HashMap<String, Any>()
                map["num1"] = BigDecimal(entryA.text.toString())
                map["string2"] = entryB.text.toString()
                map["float0"] = entryC.text.toString().toFloat()
                map["int0"] = entryD.text.toString().toInt()

                startEval(evaluationEt.text.toString(), map)
            } catch (e: Exception) {

            }
        }
    }

    private fun startEval(
        expression: String,
        map: HashMap<String, Any>
    ) {

        val measuredTime = measureTimeMillis {
            val result = mathJsWrapper.eval(expression, map)
            resultTv.text = BigDecimal(result).toPlainString()
        }

        varListTv.text =
            mathJsWrapper.getExpressionNodes(expression, NodeType.SymbolNode).toString()

        Log.d(TAG, "Measured Time: $measuredTime")
        timeTv.text = getString(R.string.execution_time_d, measuredTime.toString())
    }

    override fun onDestroy() {
        mathJsWrapper.onDestroy()
        super.onDestroy()
    }
}

// (4+(5*cos(35))-1+sqrt(4)/1)
