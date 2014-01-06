/*
* generated by Xtext
*/
grammar InternalConditionModel;

options {
	superClass=AbstractInternalAntlrParser;
	
}

@lexer::header {
package org.bonitasoft.studio.condition.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package org.bonitasoft.studio.condition.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.bonitasoft.studio.condition.services.ConditionModelGrammarAccess;

}

@parser::members {

 	private ConditionModelGrammarAccess grammarAccess;
 	
    public InternalConditionModelParser(TokenStream input, ConditionModelGrammarAccess grammarAccess) {
        this(input);
        this.grammarAccess = grammarAccess;
        registerRules(grammarAccess.getGrammar());
    }
    
    @Override
    protected String getFirstRuleName() {
    	return "Operation_Compare";	
   	}
   	
   	@Override
   	protected ConditionModelGrammarAccess getGrammarAccess() {
   		return grammarAccess;
   	}
}

@rulecatch { 
    catch (RecognitionException re) { 
        recover(input,re); 
        appendSkippedTokens();
    } 
}




// Entry rule entryRuleOperation_Compare
entryRuleOperation_Compare returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperation_CompareRule()); }
	 iv_ruleOperation_Compare=ruleOperation_Compare 
	 { $current=$iv_ruleOperation_Compare.current; } 
	 EOF 
;

// Rule Operation_Compare
ruleOperation_Compare returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getOperation_CompareAccess().getOperation_CompareAction_0(),
            $current);
    }
)(
(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_CompareAccess().getOpOperationParserRuleCall_1_0_0()); 
	    }
		lv_op_1_1=ruleOperation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_CompareRule());
	        }
       		set(
       			$current, 
       			"op",
        		lv_op_1_1, 
        		"Operation");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getOperation_CompareAccess().getOpUnary_OperationParserRuleCall_1_0_1()); 
	    }
		lv_op_1_2=ruleUnary_Operation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_CompareRule());
	        }
       		set(
       			$current, 
       			"op",
        		lv_op_1_2, 
        		"Unary_Operation");
	        afterParserOrEnumRuleCall();
	    }

)

)
))
;





// Entry rule entryRuleUnary_Operation
entryRuleUnary_Operation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getUnary_OperationRule()); }
	 iv_ruleUnary_Operation=ruleUnary_Operation 
	 { $current=$iv_ruleUnary_Operation.current; } 
	 EOF 
;

// Rule Unary_Operation
ruleUnary_Operation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getUnary_OperationAccess().getOperation_UnaryParserRuleCall_0()); 
    }
    this_Operation_Unary_0=ruleOperation_Unary
    { 
        $current = $this_Operation_Unary_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getUnary_OperationAccess().getOperation_NotUnaryParserRuleCall_1()); 
    }
    this_Operation_NotUnary_1=ruleOperation_NotUnary
    { 
        $current = $this_Operation_NotUnary_1.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleOperation
entryRuleOperation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperationRule()); }
	 iv_ruleOperation=ruleOperation 
	 { $current=$iv_ruleOperation.current; } 
	 EOF 
;

// Rule Operation
ruleOperation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getOperationAccess().getOperation_Less_EqualsParserRuleCall_0()); 
    }
    this_Operation_Less_Equals_0=ruleOperation_Less_Equals
    { 
        $current = $this_Operation_Less_Equals_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getOperationAccess().getOperation_LessParserRuleCall_1()); 
    }
    this_Operation_Less_1=ruleOperation_Less
    { 
        $current = $this_Operation_Less_1.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getOperationAccess().getOperation_Greater_EqualsParserRuleCall_2()); 
    }
    this_Operation_Greater_Equals_2=ruleOperation_Greater_Equals
    { 
        $current = $this_Operation_Greater_Equals_2.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getOperationAccess().getOperation_GreaterParserRuleCall_3()); 
    }
    this_Operation_Greater_3=ruleOperation_Greater
    { 
        $current = $this_Operation_Greater_3.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getOperationAccess().getOperation_Not_EqualsParserRuleCall_4()); 
    }
    this_Operation_Not_Equals_4=ruleOperation_Not_Equals
    { 
        $current = $this_Operation_Not_Equals_4.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getOperationAccess().getOperation_EqualsParserRuleCall_5()); 
    }
    this_Operation_Equals_5=ruleOperation_Equals
    { 
        $current = $this_Operation_Equals_5.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleOperation_Less_Equals
entryRuleOperation_Less_Equals returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperation_Less_EqualsRule()); }
	 iv_ruleOperation_Less_Equals=ruleOperation_Less_Equals 
	 { $current=$iv_ruleOperation_Less_Equals.current; } 
	 EOF 
;

// Rule Operation_Less_Equals
ruleOperation_Less_Equals returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getOperation_Less_EqualsAccess().getOperation_Less_EqualsAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_Less_EqualsAccess().getLeftExpression_TerminalParserRuleCall_1_0()); 
	    }
		lv_left_1_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_Less_EqualsRule());
	        }
       		set(
       			$current, 
       			"left",
        		lv_left_1_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='<=' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getOperation_Less_EqualsAccess().getLessThanSignEqualsSignKeyword_2());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_Less_EqualsAccess().getRightExpression_TerminalParserRuleCall_3_0()); 
	    }
		lv_right_3_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_Less_EqualsRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleOperation_Less
entryRuleOperation_Less returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperation_LessRule()); }
	 iv_ruleOperation_Less=ruleOperation_Less 
	 { $current=$iv_ruleOperation_Less.current; } 
	 EOF 
;

// Rule Operation_Less
ruleOperation_Less returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getOperation_LessAccess().getOperation_LessAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_LessAccess().getLeftExpression_TerminalParserRuleCall_1_0()); 
	    }
		lv_left_1_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_LessRule());
	        }
       		set(
       			$current, 
       			"left",
        		lv_left_1_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='<' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getOperation_LessAccess().getLessThanSignKeyword_2());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_LessAccess().getRightExpression_TerminalParserRuleCall_3_0()); 
	    }
		lv_right_3_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_LessRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleOperation_Greater_Equals
entryRuleOperation_Greater_Equals returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperation_Greater_EqualsRule()); }
	 iv_ruleOperation_Greater_Equals=ruleOperation_Greater_Equals 
	 { $current=$iv_ruleOperation_Greater_Equals.current; } 
	 EOF 
;

// Rule Operation_Greater_Equals
ruleOperation_Greater_Equals returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getOperation_Greater_EqualsAccess().getOperation_Greater_EqualsAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_Greater_EqualsAccess().getLeftExpression_TerminalParserRuleCall_1_0()); 
	    }
		lv_left_1_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_Greater_EqualsRule());
	        }
       		set(
       			$current, 
       			"left",
        		lv_left_1_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='>=' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getOperation_Greater_EqualsAccess().getGreaterThanSignEqualsSignKeyword_2());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_Greater_EqualsAccess().getRightExpression_TerminalParserRuleCall_3_0()); 
	    }
		lv_right_3_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_Greater_EqualsRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleOperation_Greater
entryRuleOperation_Greater returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperation_GreaterRule()); }
	 iv_ruleOperation_Greater=ruleOperation_Greater 
	 { $current=$iv_ruleOperation_Greater.current; } 
	 EOF 
;

// Rule Operation_Greater
ruleOperation_Greater returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getOperation_GreaterAccess().getOperation_GreaterAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_GreaterAccess().getLeftExpression_TerminalParserRuleCall_1_0()); 
	    }
		lv_left_1_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_GreaterRule());
	        }
       		set(
       			$current, 
       			"left",
        		lv_left_1_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='>' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getOperation_GreaterAccess().getGreaterThanSignKeyword_2());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_GreaterAccess().getRightExpression_TerminalParserRuleCall_3_0()); 
	    }
		lv_right_3_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_GreaterRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleOperation_Not_Equals
entryRuleOperation_Not_Equals returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperation_Not_EqualsRule()); }
	 iv_ruleOperation_Not_Equals=ruleOperation_Not_Equals 
	 { $current=$iv_ruleOperation_Not_Equals.current; } 
	 EOF 
;

// Rule Operation_Not_Equals
ruleOperation_Not_Equals returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getOperation_Not_EqualsAccess().getOperation_Not_EqualsAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_Not_EqualsAccess().getLeftExpression_TerminalParserRuleCall_1_0()); 
	    }
		lv_left_1_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_Not_EqualsRule());
	        }
       		set(
       			$current, 
       			"left",
        		lv_left_1_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='!=' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getOperation_Not_EqualsAccess().getExclamationMarkEqualsSignKeyword_2());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_Not_EqualsAccess().getRightExpression_TerminalParserRuleCall_3_0()); 
	    }
		lv_right_3_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_Not_EqualsRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleOperation_Equals
entryRuleOperation_Equals returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperation_EqualsRule()); }
	 iv_ruleOperation_Equals=ruleOperation_Equals 
	 { $current=$iv_ruleOperation_Equals.current; } 
	 EOF 
;

// Rule Operation_Equals
ruleOperation_Equals returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getOperation_EqualsAccess().getOperation_EqualsAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_EqualsAccess().getLeftExpression_TerminalParserRuleCall_1_0()); 
	    }
		lv_left_1_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_EqualsRule());
	        }
       		set(
       			$current, 
       			"left",
        		lv_left_1_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='==' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getOperation_EqualsAccess().getEqualsSignEqualsSignKeyword_2());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_EqualsAccess().getRightExpression_TerminalParserRuleCall_3_0()); 
	    }
		lv_right_3_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_EqualsRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleOperation_Unary
entryRuleOperation_Unary returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperation_UnaryRule()); }
	 iv_ruleOperation_Unary=ruleOperation_Unary 
	 { $current=$iv_ruleOperation_Unary.current; } 
	 EOF 
;

// Rule Operation_Unary
ruleOperation_Unary returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getOperation_UnaryAccess().getOperation_UnaryAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_UnaryAccess().getValueExpression_TerminalParserRuleCall_1_0()); 
	    }
		lv_value_1_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_UnaryRule());
	        }
       		set(
       			$current, 
       			"value",
        		lv_value_1_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleOperation_NotUnary
entryRuleOperation_NotUnary returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOperation_NotUnaryRule()); }
	 iv_ruleOperation_NotUnary=ruleOperation_NotUnary 
	 { $current=$iv_ruleOperation_NotUnary.current; } 
	 EOF 
;

// Rule Operation_NotUnary
ruleOperation_NotUnary returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getOperation_NotUnaryAccess().getOperation_NotUnaryAction_0(),
            $current);
    }
)	otherlv_1='!' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getOperation_NotUnaryAccess().getExclamationMarkKeyword_1());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getOperation_NotUnaryAccess().getValueExpression_TerminalParserRuleCall_2_0()); 
	    }
		lv_value_2_0=ruleExpression_Terminal		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOperation_NotUnaryRule());
	        }
       		set(
       			$current, 
       			"value",
        		lv_value_2_0, 
        		"Expression_Terminal");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleExpression_Terminal
entryRuleExpression_Terminal returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getExpression_TerminalRule()); }
	 iv_ruleExpression_Terminal=ruleExpression_Terminal 
	 { $current=$iv_ruleExpression_Terminal.current; } 
	 EOF 
;

// Rule Expression_Terminal
ruleExpression_Terminal returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getExpression_TerminalAccess().getExpression_DoubleParserRuleCall_0()); 
    }
    this_Expression_Double_0=ruleExpression_Double
    { 
        $current = $this_Expression_Double_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getExpression_TerminalAccess().getExpression_IntegerParserRuleCall_1()); 
    }
    this_Expression_Integer_1=ruleExpression_Integer
    { 
        $current = $this_Expression_Integer_1.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getExpression_TerminalAccess().getExpression_BooleanParserRuleCall_2()); 
    }
    this_Expression_Boolean_2=ruleExpression_Boolean
    { 
        $current = $this_Expression_Boolean_2.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getExpression_TerminalAccess().getExpression_StringParserRuleCall_3()); 
    }
    this_Expression_String_3=ruleExpression_String
    { 
        $current = $this_Expression_String_3.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getExpression_TerminalAccess().getExpression_ProcessRefParserRuleCall_4()); 
    }
    this_Expression_ProcessRef_4=ruleExpression_ProcessRef
    { 
        $current = $this_Expression_ProcessRef_4.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleExpression_Double
entryRuleExpression_Double returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getExpression_DoubleRule()); }
	 iv_ruleExpression_Double=ruleExpression_Double 
	 { $current=$iv_ruleExpression_Double.current; } 
	 EOF 
;

// Rule Expression_Double
ruleExpression_Double returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getExpression_DoubleAccess().getExpression_DoubleAction_0(),
            $current);
    }
)(
(
		lv_value_1_0=RULE_DOUBLE
		{
			newLeafNode(lv_value_1_0, grammarAccess.getExpression_DoubleAccess().getValueDOUBLETerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getExpression_DoubleRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"value",
        		lv_value_1_0, 
        		"DOUBLE");
	    }

)
))
;





// Entry rule entryRuleExpression_Integer
entryRuleExpression_Integer returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getExpression_IntegerRule()); }
	 iv_ruleExpression_Integer=ruleExpression_Integer 
	 { $current=$iv_ruleExpression_Integer.current; } 
	 EOF 
;

// Rule Expression_Integer
ruleExpression_Integer returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getExpression_IntegerAccess().getExpression_IntegerAction_0(),
            $current);
    }
)(
(
		lv_value_1_0=RULE_LONG
		{
			newLeafNode(lv_value_1_0, grammarAccess.getExpression_IntegerAccess().getValueLONGTerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getExpression_IntegerRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"value",
        		lv_value_1_0, 
        		"LONG");
	    }

)
))
;





// Entry rule entryRuleExpression_String
entryRuleExpression_String returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getExpression_StringRule()); }
	 iv_ruleExpression_String=ruleExpression_String 
	 { $current=$iv_ruleExpression_String.current; } 
	 EOF 
;

// Rule Expression_String
ruleExpression_String returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getExpression_StringAccess().getExpression_StringAction_0(),
            $current);
    }
)(
(
		lv_value_1_0=RULE_STRING
		{
			newLeafNode(lv_value_1_0, grammarAccess.getExpression_StringAccess().getValueSTRINGTerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getExpression_StringRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"value",
        		lv_value_1_0, 
        		"STRING");
	    }

)
))
;





// Entry rule entryRuleExpression_ProcessRef
entryRuleExpression_ProcessRef returns [EObject current=null] 
	@init { 
		HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens("RULE_WS");
	}
	:
	{ newCompositeNode(grammarAccess.getExpression_ProcessRefRule()); }
	 iv_ruleExpression_ProcessRef=ruleExpression_ProcessRef 
	 { $current=$iv_ruleExpression_ProcessRef.current; } 
	 EOF 
;
finally {
	myHiddenTokenState.restore();
}

// Rule Expression_ProcessRef
ruleExpression_ProcessRef returns [EObject current=null] 
    @init { enterRule(); 
		HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens("RULE_WS");
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getExpression_ProcessRefAccess().getExpression_ProcessRefAction_0(),
            $current);
    }
)(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getExpression_ProcessRefRule());
	        }
        }
	otherlv_1=RULE_ID
	{
		newLeafNode(otherlv_1, grammarAccess.getExpression_ProcessRefAccess().getValueEObjectCrossReference_1_0()); 
	}

)
))
;
finally {
	myHiddenTokenState.restore();
}





// Entry rule entryRuleExpression_Boolean
entryRuleExpression_Boolean returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getExpression_BooleanRule()); }
	 iv_ruleExpression_Boolean=ruleExpression_Boolean 
	 { $current=$iv_ruleExpression_Boolean.current; } 
	 EOF 
;

// Rule Expression_Boolean
ruleExpression_Boolean returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getExpression_BooleanAccess().getExpression_BooleanAction_0(),
            $current);
    }
)(
(
		lv_value_1_0=RULE_BOOLEAN
		{
			newLeafNode(lv_value_1_0, grammarAccess.getExpression_BooleanAccess().getValueBOOLEANTerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getExpression_BooleanRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"value",
        		lv_value_1_0, 
        		"BOOLEAN");
	    }

)
))
;





RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_BOOLEAN : ('true'|'false');

RULE_DATE : '\'' ~('\'')* '\'';

RULE_LONG : '-'? ('0'..'9')+;

RULE_DOUBLE : '-'? RULE_INT '.' RULE_INT;

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_INT : ('0'..'9')+;

RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_ANY_OTHER : .;


