/* Generated By:JJTree: Do not edit this line. ASTNoOpStatement.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package us.terebi.lang.lpc.parser.ast;

import us.terebi.lang.lpc.parser.jj.*;

public
class ASTNoOpStatement extends StatementNode {
  public ASTNoOpStatement(int id) {
    super(id);
  }

  public ASTNoOpStatement(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=000ad41e2b14199be6c3a2e0dc08e6b0 (do not edit this line) */
