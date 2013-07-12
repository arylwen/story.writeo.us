package us.terebi.lang.lpc.parser.ast;

public interface ParserVisitor
{
	public Object visit(ASTClassBody node, Object data);
}
