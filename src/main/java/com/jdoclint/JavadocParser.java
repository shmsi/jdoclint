package com.jdoclint;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Javadoc parser class.
 */
class JavadocParser {
    private String javadoc;

    // TODO support multiline params or return description.

    private static String PARAM_RE = "^*\\s@param\\s+(\\w+)\\s+(.*)";
    private static String THROWS_RE = "^*\\s@throws\\s+(\\w+)\\s+(.*)";
    private static String RETURN_RE = "^*\\s@return\\s+(.*)";
    private static String DESCRIPTION_RE = "^*\\s(.*)(?:(?:\\* @)|(?:\\*/))";

    /**
     * Constructor
     *
     * @param javadoc a javadoc string.
     */
    public JavadocParser(String javadoc) {
        this.javadoc = javadoc;
    }

    private List<Tuple> getparamsOrThrows(String rgex) {
        List<Tuple> paramsOrThrows = new ArrayList<>();
        Matcher matcher = getMatcher(rgex, this.javadoc);
        while (matcher.find()) {
            Tuple param = new Tuple();
            param.firstValue = matcher.group(1);
            param.secondValue = matcher.group(2);
            paramsOrThrows.add(param);
        }
        return paramsOrThrows;
    }

    /**
     * Get param list in the javadoc string.
     *
     * @return list of params.
     */
    public List<Tuple> getParams() {
        return getparamsOrThrows(PARAM_RE);
    }

    /**
     * Get return text in javadoc.
     *
     * @return return text.
     */
    public String getReturn() {
        Matcher matcher = getMatcher(RETURN_RE, this.javadoc);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Get description of function or class in the javadoc.
     *
     * @return description text.
     */
    public String getDescription() {

        // FIXME incomplete
        Pattern pattern = Pattern.compile(DESCRIPTION_RE, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(this.javadoc);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public List<Tuple> getThrows() {
        return getparamsOrThrows(THROWS_RE);
    }

    /**
     * Creates a matcher object for given regex and text.
     *
     * @param regex regular expression.
     * @param text  text.
     * @return {@link Matcher} object.
     */
    private static Matcher getMatcher(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(text);
    }

    /**
     * A tuple object to store two variables.
     */
    class Tuple {
        String firstValue;
        String secondValue;
    }

    public static void main(String[] args) {
        String javadoc = "/*\n";
        javadoc += "* this is a description\n";
        javadoc += "* this is a additional description\n";
        javadoc += "* @param arg arg description\n";
        javadoc += "*/";

        JavadocParser javadocParser = new JavadocParser(javadoc);
        System.out.println(javadocParser.getDescription());

    }
}