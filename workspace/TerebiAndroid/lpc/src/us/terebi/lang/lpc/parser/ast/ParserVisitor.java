package us.terebi.lang.lpc.parser.ast;

public interface ParserVisitor
{
	public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.SimpleNode p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTObjectDefinition p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTInherit p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTDeclaration p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTArrayStar p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTFields p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTVariable p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTVariableAssignment p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTMethod p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTModifiers p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTType p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTIdentifier p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTParameterDeclarations p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTParameterDeclaration p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTRef p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTFullType p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTClassBody p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTStatementBlock p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTNoOpStatement p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTExpressionStatement p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTLabel p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTConditionalStatement p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTLoopStatement p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTOptVariableOrExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTOptExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTControlStatement p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTVariableDeclaration p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTCompoundExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTAssignmentExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTAssignmentOperator p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTTernaryExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTLogicalOrExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTLogicalAndExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTBinaryOrExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTExclusiveOrExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTBinaryAndExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTComparisonExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTComparisonOperator p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTArithmeticExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTArithmeticOperator p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTCastExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTUnaryExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTUnaryOperator p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTPrefixIncrementOperator p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTPostfixIncrementOperator p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTPostfixExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTCallOther p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTFunctionCall p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTVariableReference p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTImmediateExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTScopedIdentifier p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTScopeResolution p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTExpressionCall p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTCatch p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTIndexPostfix p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTRange p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTIndexExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTReverseIndex p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTFunctionArguments p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTArgumentExpression p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTLiteralValue p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTConstant p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTArrayLiteral p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTArrayElement p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTElementExpander p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTMappingLiteral p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTMappingElement p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTFunctionLiteral p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTClassLiteral p1, java.lang.Object p2);

    public abstract java.lang.Object visit(us.terebi.lang.lpc.parser.ast.ASTClassElement p1, java.lang.Object p2);
	
}
