/* Generated By:JJTree: Do not edit this line. ASTPostfixExpression.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package us.terebi.lang.lpc.parser.ast;

import us.terebi.lang.lpc.parser.jj.*;

public
class ASTPostfixExpression extends ExpressionNode {
  public ASTPostfixExpression(int id) {
    super(id);
  }

  public ASTPostfixExpression(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9317c55de3c393f7b5deb829d61615e5 (do not edit this line) */
