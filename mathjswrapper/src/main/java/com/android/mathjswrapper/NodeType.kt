package com.android.mathjswrapper


sealed class NodeType(val type: String) {
    object SymbolNode : NodeType("SymbolNode")
    object OperatorNode : NodeType("OperatorNode")
    object ConstantNode : NodeType("ConstantNode")
    object ParenthesisNode : NodeType("ParenthesisNode")
}
