<?xml version="1.0"?>
<!DOCTYPE module PUBLIC
          "-//Puppy Crawl//DTD Check Configuration 1.3//EN"
          "http://www.puppycrawl.com/dtds/configuration_1_3.dtd">

<!--

  Checkstyle configuration that checks the sun coding conventions from:

    - the Java Language Specification at
      http://java.sun.com/docs/books/jls/second_edition/html/index.html

    - the Sun Code Conventions at http://java.sun.com/docs/codeconv/

    - the Javadoc guidelines at
      http://java.sun.com/j2se/javadoc/writingdoccomments/index.html

    - the JDK Api documentation http://java.sun.com/j2se/docs/api/index.html

    - some best practices

  Checkstyle is very configurable. Be sure to read the documentation at
  http://checkstyle.sf.net (or in your downloaded distribution).

  Most Checks are configurable, be sure to consult the documentation.

  To completely disable a check, just comment it out or delete it from the file.

  Finally, it is worth reading the documentation.

-->

<module name="Checker">
    <!--
        If you set the basedir property below, then all reported file
        names will be relative to the specified directory. See
        http://checkstyle.sourceforge.net/5.x/config.html#Checker

        <property name="basedir" value="${r"${basedir}"}"/>
    -->

    <!-- Checks that a package-info.java file exists for each package.     -->
    <!-- See http://checkstyle.sf.net/config_javadoc.html#JavadocPackage -->
    <module name="JavadocPackage"/>

    <!-- Checks whether files end with a new line.                        -->
    <!-- See http://checkstyle.sf.net/config_misc.html#NewlineAtEndOfFile -->
    <module name="NewlineAtEndOfFile"/>

    <!-- Checks that property files contain the same keys.         -->
    <!-- See http://checkstyle.sf.net/config_misc.html#Translation -->
    <module name="Translation"/>
    
    <!-- Checks for Size Violations.                    -->
    <!-- See http://checkstyle.sf.net/config_sizes.html -->
    <module name="FileLength"/>
    
    <!-- Checks for Headers                                -->
    <!-- See http://checkstyle.sf.net/config_header.html   -->
    <!-- <module name="Header"> -->
    <!--   <property name="headerFile" value="${r"${checkstyle.header.file}"}"/> -->
    <!--   <property name="fileExtensions" value="java"/> -->
    <!-- </module> -->

    <module name="TreeWalker">

        <!-- Checks for Javadoc comments.                     -->
        <!-- See http://checkstyle.sf.net/config_javadoc.html -->
        <module name="JavadocMethod"/>
        <property name="tabWidth" value="4"/>
        <module name="JavadocType"/>
        <module name="JavadocVariable"/>
        <module name="JavadocStyle"/>


        <!-- Checks for Naming Conventions.                  -->
        <!-- See http://checkstyle.sf.net/config_naming.html -->
        <module name="ConstantName"/>
        <module name="LocalFinalVariableName"/>
        <module name="LocalVariableName"/>
        <module name="MemberName"/>
        <module name="MethodName"/>
        <module name="PackageName"/>
        <module name="ParameterName"/>
        <module name="StaticVariableName"/>
        <module name="TypeName"/>


        <!-- Checks for imports                              -->
        <!-- See http://checkstyle.sf.net/config_import.html -->
        <module name="AvoidStarImport"/>
        <module name="IllegalImport"/> <!-- defaults to sun.* packages -->
        <module name="RedundantImport"/>
        <module name="UnusedImports"/>


        <!-- Checks for Size Violations.                    -->
        <!-- See http://checkstyle.sf.net/config_sizes.html -->
        <module name="LineLength"/>
        <module name="MethodLength"/>
        <module name="ParameterNumber"/>


        <!-- Checks for whitespace                               -->
        <!-- See http://checkstyle.sf.net/config_whitespace.html -->
        <module name="EmptyForIteratorPad"/>
        <module name="GenericWhitespace"/>
        <module name="MethodParamPad"/>
        <module name="NoWhitespaceAfter"/>
        <module name="NoWhitespaceBefore"/>
        <module name="OperatorWrap"/>
        <module name="ParenPad"/>
        <module name="TypecastParenPad"/>
        <module name="WhitespaceAfter"/>
        <module name="WhitespaceAround"/>


        <!-- Modifier Checks                                    -->
        <!-- See http://checkstyle.sf.net/config_modifiers.html -->
        <module name="ModifierOrder"/>
        <module name="RedundantModifier"/>


        <!-- Checks for blocks. You know, those {}'s         -->
        <!-- See http://checkstyle.sf.net/config_blocks.html -->
        <module name="AvoidNestedBlocks"/>
        <module name="EmptyBlock"/>
        <module name="LeftCurly"/>
        <module name="NeedBraces"/>
        <module name="RightCurly"/>


        <!-- Checks for common coding problems               -->
        <!-- See http://checkstyle.sf.net/config_coding.html -->
        <module name="AvoidInlineConditionals"/>
<!--        <module name="DoubleCheckedLocking"/>  -->  <!-- MY FAVOURITE -->
        <module name="EmptyStatement"/>
        <module name="EqualsHashCode"/>
        <module name="HiddenField">
		<property name="ignoreConstructorParameter" value="true"/>
		<property name="ignoreSetter" value="true"/>
	</module>
        <module name="IllegalInstantiation"/>
        <module name="InnerAssignment"/>
        <module name="MagicNumber"/>
        <module name="MissingSwitchDefault"/>
        <module name="RedundantThrows"/>
        <module name="SimplifyBooleanExpression"/>
        <module name="SimplifyBooleanReturn"/>

        <!-- Checks for class design                         -->
        <!-- See http://checkstyle.sf.net/config_design.html -->
        <module name="FinalClass"/>
        <module name="HideUtilityClassConstructor"/>
        <module name="InterfaceIsType"/>
        <module name="VisibilityModifier"/>


        <!-- Miscellaneous other checks.                   -->
        <!-- See http://checkstyle.sf.net/config_misc.html -->
        <module name="ArrayTypeStyle"/>
        <module name="TodoComment"/>
        <module name="UpperEll"/>

    </module>

</module>
