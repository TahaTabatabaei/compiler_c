package Compiler;

import gen.CListener;
import gen.CParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Stack;

public class ProgramPrinter  implements CListener {
    ErrorHandler errorHandler = new ErrorHandler();
    GlobalTable programTable= new GlobalTable();
    Stack<SymbolTable> currentScopes = new Stack<SymbolTable>();
    int indentCount = 0;
    private String retrunType;

    private String indentation(int n) {
        String retIndent = "";
        String indent = "    ";
        for (int i = 1; i <= n; i++)
            retIndent += indent;

        return retIndent;
    }

    // search method to find a key in HashTables
    boolean searchKey(String key){
        SymbolTable st = currentScopes.peek();
        while (st != null) {
            if (st.map.containsKey(key)){
                return true;
            }
            st = st.parentNode;
        }
        return false;
    }

    String getValue(String key){
        SymbolTable st = currentScopes.peek();
        while (st != null) {
            if (st.map.containsKey(key)){
                return st.map.get(key).toString();
            }
            st = st.parentNode;
        }
        return "";
    }

    // flag to see if we should look for valid return type or not
    boolean return_flag = false;
    // flag to see if we should compare the returnType and function return attribute
    boolean isReturn_flag = false;
    int lineNum = 0, colNum = 0;


    int nestedCounter = 0;
    ArrayList<TerminalNode> methods = new ArrayList<TerminalNode>();

    @Override
    public void enterPrimaryExpression(CParser.PrimaryExpressionContext ctx) {

        String value = "";
        if(ctx.Identifier() != null){
            String identifier = ctx.Identifier().getText();


            StringBuilder varKey = new StringBuilder();
            StringBuilder funcKey = new StringBuilder();

            varKey.append("Field_" + identifier);
            // check if it is variable name existing in table
            if (!searchKey(varKey.toString())){
                // check if it is variable name existing in table
                funcKey.append("Method_" + identifier);
                if(!searchKey(funcKey.toString())){
                String message = errorHandler.errorMaker(106,"\"Undefined variable:",ctx.start.getLine()
                        ,ctx.start.getCharPositionInLine(),identifier,"\" can not find variable/method.");
                errorHandler.errors.add(message);

                }
            }else{
                value = getValue(varKey.toString());
            }
        }

        if (return_flag){
            this.isReturn_flag = true;
            if(ctx.Identifier() != null){
                this.retrunType = value;
            }else if (ctx.Constant() != null){
                this.retrunType = "constant";
            }else if (ctx.StringLiteral().size() > 0){
                this.retrunType = "char";
            }
            // get line and column index
            this.lineNum = ctx.start.getLine();
            this.colNum = ctx.start.getCharPositionInLine();
        }
    }

    @Override
    public void exitPrimaryExpression(CParser.PrimaryExpressionContext ctx) {

    }

    @Override
    public void enterPostfixExpression(CParser.PostfixExpressionContext ctx) {
        if (!ctx.LeftParen().isEmpty() && !ctx.RightParen().isEmpty()){
            System.out.print(indentation(this.indentCount++) + "function call: ");

            String name = ctx.primaryExpression().Identifier().getText();

            System.out.print("name: " + name + "/ params: ");

            StringBuilder sb = new StringBuilder();

            int index = 0;
            for (CParser.ArgumentExpressionListContext arex : ctx.argumentExpressionList()){
                for (CParser.AssignmentExpressionContext asex : arex.assignmentExpression()) {
                    String param = asex.getText();
                    sb.append(" " + param + " (index= " + index++ + ")");
                }
            }

            if (index>0){
                System.out.println(sb.toString());
            }
        }


    }

    @Override
    public void exitPostfixExpression(CParser.PostfixExpressionContext ctx) {
        if (!ctx.LeftParen().isEmpty() && !ctx.RightParen().isEmpty()) {
            this.indentCount--;
        }

    }

    @Override
    public void enterArgumentExpressionList(CParser.ArgumentExpressionListContext ctx) {

    }

    @Override
    public void exitArgumentExpressionList(CParser.ArgumentExpressionListContext ctx) {

    }

    @Override
    public void enterUnaryExpression(CParser.UnaryExpressionContext ctx) {

    }

    @Override
    public void exitUnaryExpression(CParser.UnaryExpressionContext ctx) {

    }

    @Override
    public void enterUnaryOperator(CParser.UnaryOperatorContext ctx) {

    }

    @Override
    public void exitUnaryOperator(CParser.UnaryOperatorContext ctx) {

    }

    @Override
    public void enterCastExpression(CParser.CastExpressionContext ctx) {

    }

    @Override
    public void exitCastExpression(CParser.CastExpressionContext ctx) {

    }

    @Override
    public void enterMultiplicativeExpression(CParser.MultiplicativeExpressionContext ctx) {

    }

    @Override
    public void exitMultiplicativeExpression(CParser.MultiplicativeExpressionContext ctx) {

    }

    @Override
    public void enterAdditiveExpression(CParser.AdditiveExpressionContext ctx) {

    }

    @Override
    public void exitAdditiveExpression(CParser.AdditiveExpressionContext ctx) {

    }

    @Override
    public void enterShiftExpression(CParser.ShiftExpressionContext ctx) {

    }

    @Override
    public void exitShiftExpression(CParser.ShiftExpressionContext ctx) {

    }

    @Override
    public void enterRelationalExpression(CParser.RelationalExpressionContext ctx) {

    }

    @Override
    public void exitRelationalExpression(CParser.RelationalExpressionContext ctx) {

    }

    @Override
    public void enterEqualityExpression(CParser.EqualityExpressionContext ctx) {

    }

    @Override
    public void exitEqualityExpression(CParser.EqualityExpressionContext ctx) {

    }

    @Override
    public void enterAndExpression(CParser.AndExpressionContext ctx) {

    }

    @Override
    public void exitAndExpression(CParser.AndExpressionContext ctx) {

    }

    @Override
    public void enterExclusiveOrExpression(CParser.ExclusiveOrExpressionContext ctx) {

    }

    @Override
    public void exitExclusiveOrExpression(CParser.ExclusiveOrExpressionContext ctx) {

    }

    @Override
    public void enterInclusiveOrExpression(CParser.InclusiveOrExpressionContext ctx) {

    }

    @Override
    public void exitInclusiveOrExpression(CParser.InclusiveOrExpressionContext ctx) {

    }

    @Override
    public void enterLogicalAndExpression(CParser.LogicalAndExpressionContext ctx) {

    }

    @Override
    public void exitLogicalAndExpression(CParser.LogicalAndExpressionContext ctx) {

    }

    @Override
    public void enterLogicalOrExpression(CParser.LogicalOrExpressionContext ctx) {

    }

    @Override
    public void exitLogicalOrExpression(CParser.LogicalOrExpressionContext ctx) {

    }

    @Override
    public void enterConditionalExpression(CParser.ConditionalExpressionContext ctx) {

    }

    @Override
    public void exitConditionalExpression(CParser.ConditionalExpressionContext ctx) {

    }

    @Override
    public void enterAssignmentExpression(CParser.AssignmentExpressionContext ctx) {

    }

    @Override
    public void exitAssignmentExpression(CParser.AssignmentExpressionContext ctx) {

    }

    @Override
    public void enterAssignmentOperator(CParser.AssignmentOperatorContext ctx) {

    }

    @Override
    public void exitAssignmentOperator(CParser.AssignmentOperatorContext ctx) {

    }

    @Override
    public void enterExpression(CParser.ExpressionContext ctx) {

    }

    @Override
    public void exitExpression(CParser.ExpressionContext ctx) {

    }

    @Override
    public void enterConstantExpression(CParser.ConstantExpressionContext ctx) {

    }

    @Override
    public void exitConstantExpression(CParser.ConstantExpressionContext ctx) {

    }

    @Override
    public void enterDeclaration(CParser.DeclarationContext ctx) {

        System.out.print(indentation(this.indentCount) + "field:");


        String typeQual = "";

        StringBuilder sb = new StringBuilder();

        String typeSpec = ctx.declarationSpecifiers().declarationSpecifier(0).getText();

        if(ctx.declarationSpecifiers().declarationSpecifier().size() > 1){
            typeQual = ctx.declarationSpecifiers().declarationSpecifier(0).getText();
            typeSpec = ctx.declarationSpecifiers().declarationSpecifier(1).getText();
        }

        for (CParser.InitDeclaratorContext idc: ctx.initDeclaratorList().initDeclarator()) {
            String name = idc.declarator().directDeclarator().Identifier().getText();

            StringBuilder key = new StringBuilder();
            StringBuilder value = new StringBuilder();

            key.append("Field_" + name);
            value.append(" " + currentScopes.peek().fieldType() + "(name : "+ name + ")" + " (type : "+typeSpec );
            sb.append(" " + name);
            sb.append("/ ");
            sb.append("type: ");
            sb.append(typeSpec);
            if(ctx.declarationSpecifiers().declarationSpecifier().size()  > 1){
                value.append(" ," + "typeQ : "+ typeQual);
                sb.append(" typeQ: ");
                sb.append(typeQual);
            }
            value.append(")");
            value.append("line_number = "+ctx.start.getLine()+"");
            if (currentScopes.peek().map.containsKey(key.toString()) &&
                    currentScopes.peek().lookUp(key.toString()).contains(typeSpec)){
                String message = errorHandler.errorMaker(104, "field", ctx.start.getLine(),ctx.start.getCharPositionInLine()
                        ,name,"has been defined already");
                errorHandler.errors.add(message);
                key.append( "_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine());
            }

            currentScopes.peek().insert(key.toString(), value.toString());
        }
        System.out.println(sb.toString());
    }

    @Override
    public void exitDeclaration(CParser.DeclarationContext ctx) {
    }

    @Override
    public void enterDeclarationSpecifiers(CParser.DeclarationSpecifiersContext ctx) {
    }

    @Override
    public void exitDeclarationSpecifiers(CParser.DeclarationSpecifiersContext ctx) {

    }

    @Override
    public void enterDeclarationSpecifier(CParser.DeclarationSpecifierContext ctx) {
    }

    @Override
    public void exitDeclarationSpecifier(CParser.DeclarationSpecifierContext ctx) {

    }

    @Override
    public void enterInitDeclaratorList(CParser.InitDeclaratorListContext ctx) {
    }

    @Override
    public void exitInitDeclaratorList(CParser.InitDeclaratorListContext ctx) {

    }

    @Override
    public void enterInitDeclarator(CParser.InitDeclaratorContext ctx) {

    }

    @Override
    public void exitInitDeclarator(CParser.InitDeclaratorContext ctx) {

    }

    @Override
    public void enterStorageClassSpecifier(CParser.StorageClassSpecifierContext ctx) {

    }

    @Override
    public void exitStorageClassSpecifier(CParser.StorageClassSpecifierContext ctx) {

    }

    @Override
    public void enterTypeSpecifier(CParser.TypeSpecifierContext ctx) {

    }

    @Override
    public void exitTypeSpecifier(CParser.TypeSpecifierContext ctx) {

    }

    @Override
    public void enterStructOrUnionSpecifier(CParser.StructOrUnionSpecifierContext ctx) {

    }

    @Override
    public void exitStructOrUnionSpecifier(CParser.StructOrUnionSpecifierContext ctx) {

    }

    @Override
    public void enterStructOrUnion(CParser.StructOrUnionContext ctx) {

    }

    @Override
    public void exitStructOrUnion(CParser.StructOrUnionContext ctx) {

    }

    @Override
    public void enterStructDeclarationList(CParser.StructDeclarationListContext ctx) {

    }

    @Override
    public void exitStructDeclarationList(CParser.StructDeclarationListContext ctx) {

    }

    @Override
    public void enterStructDeclaration(CParser.StructDeclarationContext ctx) {

    }

    @Override
    public void exitStructDeclaration(CParser.StructDeclarationContext ctx) {

    }

    @Override
    public void enterSpecifierQualifierList(CParser.SpecifierQualifierListContext ctx) {

    }

    @Override
    public void exitSpecifierQualifierList(CParser.SpecifierQualifierListContext ctx) {

    }

    @Override
    public void enterStructDeclaratorList(CParser.StructDeclaratorListContext ctx) {

    }

    @Override
    public void exitStructDeclaratorList(CParser.StructDeclaratorListContext ctx) {

    }

    @Override
    public void enterStructDeclarator(CParser.StructDeclaratorContext ctx) {

    }

    @Override
    public void exitStructDeclarator(CParser.StructDeclaratorContext ctx) {

    }

    @Override
    public void enterEnumSpecifier(CParser.EnumSpecifierContext ctx) {

    }

    @Override
    public void exitEnumSpecifier(CParser.EnumSpecifierContext ctx) {

    }

    @Override
    public void enterEnumeratorList(CParser.EnumeratorListContext ctx) {

    }

    @Override
    public void exitEnumeratorList(CParser.EnumeratorListContext ctx) {

    }

    @Override
    public void enterEnumerator(CParser.EnumeratorContext ctx) {

    }

    @Override
    public void exitEnumerator(CParser.EnumeratorContext ctx) {

    }

    @Override
    public void enterEnumerationConstant(CParser.EnumerationConstantContext ctx) {

    }

    @Override
    public void exitEnumerationConstant(CParser.EnumerationConstantContext ctx) {

    }

    @Override
    public void enterTypeQualifier(CParser.TypeQualifierContext ctx) {

    }

    @Override
    public void exitTypeQualifier(CParser.TypeQualifierContext ctx) {

    }

    @Override
    public void enterDeclarator(CParser.DeclaratorContext ctx) {

    }

    @Override
    public void exitDeclarator(CParser.DeclaratorContext ctx) {

    }

    @Override
    public void enterDirectDeclarator(CParser.DirectDeclaratorContext ctx) {

    }

    @Override
    public void exitDirectDeclarator(CParser.DirectDeclaratorContext ctx) {

    }

    @Override
    public void enterNestedParenthesesBlock(CParser.NestedParenthesesBlockContext ctx) {

    }

    @Override
    public void exitNestedParenthesesBlock(CParser.NestedParenthesesBlockContext ctx) {

    }

    @Override
    public void enterPointer(CParser.PointerContext ctx) {

    }

    @Override
    public void exitPointer(CParser.PointerContext ctx) {

    }

    @Override
    public void enterTypeQualifierList(CParser.TypeQualifierListContext ctx) {

    }

    @Override
    public void exitTypeQualifierList(CParser.TypeQualifierListContext ctx) {

    }

    @Override
    public void enterParameterTypeList(CParser.ParameterTypeListContext ctx) {

    }

    @Override
    public void exitParameterTypeList(CParser.ParameterTypeListContext ctx) {

    }

    @Override
    public void enterParameterList(CParser.ParameterListContext ctx) {

        StringBuilder sb = new StringBuilder();

        System.out.print(indentation(this.indentCount) + "parameters list: [ ");
        for(CParser.ParameterDeclarationContext cp : ctx.parameterDeclaration()){

            StringBuilder key = new StringBuilder();
            StringBuilder value = new StringBuilder();

            String name = cp.declarator().directDeclarator().Identifier().getText();
            String type = cp.declarationSpecifiers().getText();

            key.append("Field_" + name);

            if (cp.declarator().directDeclarator().Constant().size() > 0){
                value.append("methodParamField" + "(name: " + name + ")" + "(type: "+ type + " array, " + "length = " +
                        cp.declarator().directDeclarator().Constant(0) + ")");
            }else {
                value.append("methodParamField" + "(name: " + name + ")" + "(type: "+ type + ")");
            }

            sb.append(name);
            sb.append(" ");
            sb.append(type);
            sb.append(", ");

            currentScopes.peek().insert(key.toString(), value.toString());
        }
        sb.deleteCharAt(sb.length()-1); // remove last redundant space
        sb.deleteCharAt(sb.length()-1); // remove last redundant comma

        System.out.print(sb.toString());
        this.indentCount++;

    }

    @Override
    public void exitParameterList(CParser.ParameterListContext ctx) {
        System.out.println(" ]");
        this.indentCount--;
    }

    @Override
    public void enterParameterDeclaration(CParser.ParameterDeclarationContext ctx) {

    }

    @Override
    public void exitParameterDeclaration(CParser.ParameterDeclarationContext ctx) {

    }

    @Override
    public void enterIdentifierList(CParser.IdentifierListContext ctx) {

    }

    @Override
    public void exitIdentifierList(CParser.IdentifierListContext ctx) {

    }

    @Override
    public void enterTypeName(CParser.TypeNameContext ctx) {

    }

    @Override
    public void exitTypeName(CParser.TypeNameContext ctx) {

    }

    @Override
    public void enterTypedefName(CParser.TypedefNameContext ctx) {

    }

    @Override
    public void exitTypedefName(CParser.TypedefNameContext ctx) {

    }

    @Override
    public void enterInitializer(CParser.InitializerContext ctx) {

    }

    @Override
    public void exitInitializer(CParser.InitializerContext ctx) {

    }

    @Override
    public void enterInitializerList(CParser.InitializerListContext ctx) {

    }

    @Override
    public void exitInitializerList(CParser.InitializerListContext ctx) {

    }

    @Override
    public void enterDesignation(CParser.DesignationContext ctx) {

    }

    @Override
    public void exitDesignation(CParser.DesignationContext ctx) {

    }

    @Override
    public void enterDesignatorList(CParser.DesignatorListContext ctx) {

    }

    @Override
    public void exitDesignatorList(CParser.DesignatorListContext ctx) {

    }

    @Override
    public void enterDesignator(CParser.DesignatorContext ctx) {

    }

    @Override
    public void exitDesignator(CParser.DesignatorContext ctx) {

    }

    @Override
    public void enterStatement(CParser.StatementContext ctx) {

    }

    @Override
    public void exitStatement(CParser.StatementContext ctx) {

    }

    @Override
    public void enterLabeledStatement(CParser.LabeledStatementContext ctx) {

    }

    @Override
    public void exitLabeledStatement(CParser.LabeledStatementContext ctx) {

    }

    @Override
    public void enterCompoundStatement(CParser.CompoundStatementContext ctx) {

    }

    @Override
    public void exitCompoundStatement(CParser.CompoundStatementContext ctx) {

    }

    @Override
    public void enterBlockItemList(CParser.BlockItemListContext ctx) {

    }

    @Override
    public void exitBlockItemList(CParser.BlockItemListContext ctx) {

    }

    @Override
    public void enterBlockItem(CParser.BlockItemContext ctx) {

    }

    @Override
    public void exitBlockItem(CParser.BlockItemContext ctx) {

    }

    @Override
    public void enterExpressionStatement(CParser.ExpressionStatementContext ctx) {

    }

    @Override
    public void exitExpressionStatement(CParser.ExpressionStatementContext ctx) {

    }
    @Override
    public void enterSelectionStatement(CParser.SelectionStatementContext ctx) {
        BlockTable tempIf = new BlockTable();
        tempIf.parentNode = currentScopes.peek();
        tempIf.lineNumber = ctx.start.getLine();
        tempIf.tableName = "if_nested";

        if (currentScopes.peek() instanceof MethodTable){
            ((MethodTable)currentScopes.peek()).blocks.add(tempIf);

        }else {
            ((BlockTable)currentScopes.peek()).blocks.add(tempIf);
        }
        currentScopes.push(tempIf);
//        TODO: if and else separate
        nestedCounter++;
        if(nestedCounter >= 1){
            System.out.println(indentation(this.indentCount++) + "nested statement {");
        }
    }

    @Override
    public void exitSelectionStatement(CParser.SelectionStatementContext ctx) {
        currentScopes.pop();

        if(nestedCounter >= 1){
            System.out.println(indentation(--this.indentCount) + "}");
        }
        nestedCounter--;
    }

    @Override
    public void enterIterationStatement(CParser.IterationStatementContext ctx) {

        BlockTable tempLoop = new BlockTable();
        tempLoop.parentNode = currentScopes.peek();
        tempLoop.lineNumber = ctx.start.getLine();
        tempLoop.tableName = "loop_nested";

        if (currentScopes.peek() instanceof MethodTable){
            ((MethodTable)currentScopes.peek()).blocks.add(tempLoop);

        }else {
            ((BlockTable)currentScopes.peek()).blocks.add(tempLoop);
        }
        currentScopes.push(tempLoop);
        nestedCounter++;
        if(nestedCounter >= 1){
            System.out.println(indentation(this.indentCount++) + "nested statement {");
        }

    }

    @Override
    public void exitIterationStatement(CParser.IterationStatementContext ctx) {
        currentScopes.pop();

        if(nestedCounter >= 1){
            System.out.println(indentation(--this.indentCount) + "}");
        }
        nestedCounter--;
    }

    @Override
    public void enterForCondition(CParser.ForConditionContext ctx) {

    }

    @Override
    public void exitForCondition(CParser.ForConditionContext ctx) {

    }

    @Override
    public void enterForDeclaration(CParser.ForDeclarationContext ctx) {
            System.out.print(indentation(this.indentCount) + "field:");


            String typeQual = "";

            StringBuilder sb = new StringBuilder();

            String typeSpec = ctx.declarationSpecifiers().declarationSpecifier(0).getText();

            if(ctx.declarationSpecifiers().declarationSpecifier().size() > 1){
                typeQual = ctx.declarationSpecifiers().declarationSpecifier(0).getText();
                typeSpec = ctx.declarationSpecifiers().declarationSpecifier(1).getText();
            }

            for (CParser.InitDeclaratorContext idc: ctx.initDeclaratorList().initDeclarator()) {
                String name = idc.declarator().directDeclarator().Identifier().getText();

                StringBuilder key = new StringBuilder();
                StringBuilder value = new StringBuilder();

                key.append("Field_" + name);
                value.append(" " + currentScopes.peek().fieldType() + "(name : "+ name + ")" + " (type : "+typeSpec );
                sb.append(" " + name);
                sb.append("/ ");
                sb.append("type: ");
                sb.append(typeSpec);
                if(ctx.declarationSpecifiers().declarationSpecifier().size()  > 1){
                    value.append(" ," + "typeQ : "+ typeQual);
                    sb.append(" typeQ: ");
                    sb.append(typeQual);
                }
                value.append(")");
                value.append("line_number = "+ctx.start.getLine()+"");

                if (currentScopes.peek().map.containsKey(key.toString()) &&
                        currentScopes.peek().lookUp(key.toString()).contains(typeSpec)){
                    String message = errorHandler.errorMaker(104, "field", ctx.start.getLine(),ctx.start.getCharPositionInLine()
                            ,name,"has been defined already");
                    errorHandler.errors.add(message);
                    key.append( "_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine());
                }
                currentScopes.peek().insert(key.toString(), value.toString());
            }
            System.out.println(sb.toString());


    }

    @Override
    public void exitForDeclaration(CParser.ForDeclarationContext ctx) {

    }

    @Override
    public void enterForExpression(CParser.ForExpressionContext ctx) {

    }

    @Override
    public void exitForExpression(CParser.ForExpressionContext ctx) {

    }

    @Override
    public void enterJumpStatement(CParser.JumpStatementContext ctx) {
        // jump statement means that we should look for return
        if(ctx.Return() != null) {
            this.return_flag = true;
        }
    }

    @Override
    public void exitJumpStatement(CParser.JumpStatementContext ctx) {
        if(ctx.Return() != null) {
            this.return_flag = false;
            System.out.println("qqqqqqqqqqqqqq");
        }
    }

    @Override
    public void enterExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
        programTable.lineNumber = 1;
        programTable.tableName = "program";
        currentScopes.add(programTable);
        System.out.println(indentation(this.indentCount) + "program start {");
        this.indentCount++;
    }

    @Override
    public void exitExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
        this.indentCount--;
        currentScopes.pop();
        System.out.println(indentation(this.indentCount) + "}");
        programTable.tablePrinter();
        errorHandler.printErrors();
    }

    @Override
    public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        String key="";
        StringBuilder value = new StringBuilder();
        MethodTable tempmethod = new MethodTable();

        System.out.print(indentation(this.indentCount));

        String method_name = ctx.declarator().directDeclarator().directDeclarator().Identifier().getText();
        this.methods.add(ctx.declarator().directDeclarator().directDeclarator().Identifier());
        String functionName = "name: " + method_name;
        String method_type = ctx.typeSpecifier().getText();
        String typeSpecifier = "return type: " + method_type;

        if (method_name.equals("main")){
            key = "Method_main";
            System.out.println("main method: " + typeSpecifier + " {");
        }else {
            key = "Method_"+method_name;
            System.out.println("normal method: " + functionName + "/ " + typeSpecifier + " {");
        }



        StringBuilder checkParam = new StringBuilder();
        if (ctx.declarator().directDeclarator().parameterTypeList() != null){
            checkParam.append(" [parameter list: ");
            int index = 0;
            for(CParser.ParameterDeclarationContext param :
                    ctx.declarator().directDeclarator().parameterTypeList().parameterList().parameterDeclaration() ){
                String type = param.declarationSpecifiers().getText();
                if (index != 0){
                    checkParam.append(", ");
                }
                if (param.declarator().directDeclarator().Constant().size() > 0){
                    checkParam.append("[type: " + type + " array, index:" + index + "]");
                }else {
                    checkParam.append("[type: " + type + " , index:" + index + "]");
                }
                index++;
            }
            checkParam.append("]");
        }else {
            checkParam.append("approved");
        }

        if (currentScopes.peek().map.containsKey(key) &&
                currentScopes.peek().lookUp(key).contains(typeSpecifier) &&
                currentScopes.peek().lookUp(key).contains(checkParam.toString())){

            String message = errorHandler.errorMaker(102, "method", ctx.start.getLine(),ctx.start.getCharPositionInLine()
                    ,functionName,"has been defined already");
            errorHandler.errors.add(message);
            key += "_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine();
        } else if (currentScopes.peek().map.containsKey(key)) {
            key += "_"+ctx.start.getLine()+"_"+ctx.start.getCharPositionInLine();
        }

        value.append( key+ " ("+functionName+" ) "+"( "+typeSpecifier+" )");
        if (!checkParam.toString().equals("approved")){
            value.append(checkParam);
            tempmethod.paramList = checkParam.toString();
        }

        currentScopes.peek().insert(key, value.toString());
        tempmethod.parentNode=currentScopes.peek();
        tempmethod.lineNumber=ctx.start.getLine();
        tempmethod.tableName=key;
        ((GlobalTable)currentScopes.peek()).methods.add(tempmethod);
        currentScopes.push(tempmethod);

        this.indentCount++;

    }

    @Override
    public void exitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        currentScopes.pop();
        System.out.println(indentation(--this.indentCount) + "}");

        if (this.isReturn_flag){

            String type = ctx.typeSpecifier().getText();
            if (!this.retrunType.contains(type)){
                String message = errorHandler.errorMaker(210,"",this.lineNum
                        ,this.colNum," Return Type of this method must be ","< " + type + " >");
                errorHandler.errors.add(message);
            }else
            if(this.retrunType.equals("constant")){
                if (!(type.equals("int") || type.equals("double"))){
                    String message = errorHandler.errorMaker(210,"",ctx.start.getCharPositionInLine()
                            ,ctx.start.getCharPositionInLine()," ReturnType of this method must be ","< constant >");
                    errorHandler.errors.add(message);
                }
            }else
            if (this.retrunType.equals("char")){
                String message = errorHandler.errorMaker(210,"",ctx.start.getCharPositionInLine()
                        ,ctx.start.getCharPositionInLine()," ReturnType of this method must be ","< char >");
                errorHandler.errors.add(message);
            }
        }
        this.isReturn_flag = false;


    }

    @Override
    public void enterDeclarationList(CParser.DeclarationListContext ctx) {

    }

    @Override
    public void exitDeclarationList(CParser.DeclarationListContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
