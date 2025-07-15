package org.vaadin.addons.componentfactory.demo.helpers;


import com.vaadin.flow.demo.SourceCodeExample;
import com.vaadin.flow.demo.SourceCodeExample.SourceType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Modified version of {@link com.vaadin.flow.demo.SourceContentResolver} to support Spring Boot.
 */
public class SpringSourceContentResolver {

    // @formatter::off
    private static final ConcurrentHashMap<Class<? extends AbstractDemoView>, List<SourceCodeExample>> CACHED_SOURCE_EXAMPLES = new ConcurrentHashMap<>();
    // @formatter::on

    private static final Pattern SOURCE_CODE_EXAMPLE_BEGIN_PATTERN = Pattern
            .compile("\\s*// begin-source-example");
    private static final Pattern SOURCE_CODE_EXAMPLE_END_PATTERN = Pattern
            .compile("\\s*// end-source-example");
    private static final Pattern SOURCE_CODE_EXAMPLE_HEADING_PATTERN = Pattern
            .compile("\\s*// source-example-heading: (.*)");
    private static final Pattern SOURCE_CODE_EXAMPLE_TYPE_PATTERN = Pattern
            .compile("\\s*// source-example-type: ([A-Z]+)");

    private SpringSourceContentResolver() {
    }

    /**
     * Get all {@link SourceCodeExample}s from a given class.
     *
     * @param demoViewClass
     *            the class to retrieve source code examples for
     * @return an unmodifiable list of source code examples
     */
    public static List<SourceCodeExample> getSourceCodeExamplesForClass(
            Class<? extends AbstractDemoView> demoViewClass) {
        return CACHED_SOURCE_EXAMPLES.computeIfAbsent(demoViewClass,
                SpringSourceContentResolver::parseSourceCodeExamplesForClass);
    }

    private static List<SourceCodeExample> parseSourceCodeExamplesForClass(
            Class<? extends AbstractDemoView> demoViewClass) {

        String resourcePath = getResourcePath(demoViewClass);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                SpringSourceContentResolver.class.getClassLoader()
                        .getResourceAsStream(resourcePath),
                StandardCharsets.UTF_8))) {

            List<String> lines = reader.lines().collect(Collectors.toList());
            return Collections.unmodifiableList(parseSourceCodeExamples(lines));
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error reading source examples for class " + demoViewClass,
                    e);
        }
    }

    private static String getResourcePath(Class<? extends AbstractDemoView> demoViewClass) {
        String javaFile = demoViewClass.getSimpleName() + ".java";
        String formattedPackageName = demoViewClass.getPackage().getName().replace('.', '/');
        String resourcePath = formattedPackageName + "/" + javaFile;

        if (demoViewClass.getClassLoader().getResource(resourcePath) == null) {
            throw new IllegalArgumentException(
                    "Could not find file resources for '" + demoViewClass.getName() + "'. Ensure that you are copying " +
                            "the source files to the classpath in your POM like this:\n" +
                            "<build>\n" +
                            "    <resources>\n" +
                            "        <resource>\n" +
                            "            <directory>src/main/java</directory>\n" +
                            "            <includes>\n" +
                            "                <include>**/*.java</include>\n" +
                            "            </includes>\n" +
                            "        </resource>\n" +
                            "    </resources>\n" +
                            "</build>");
        }
        return resourcePath;

    }

    private static boolean isAvailable(String resourcePath) {
        return SpringSourceContentResolver.class.getClassLoader()
                .getResource(resourcePath) != null;
    }

    private static List<SourceCodeExample> parseSourceCodeExamples(
            List<String> sourceLines) {
        List<SourceCodeExample> examples = new ArrayList<>();
        int startIndex = -1;
        int endIndex = -1;
        for (int i = 0; i < sourceLines.size(); i++) {
            if (SOURCE_CODE_EXAMPLE_BEGIN_PATTERN.matcher(sourceLines.get(i))
                    .matches()) {
                startIndex = i;
            } else if (SOURCE_CODE_EXAMPLE_END_PATTERN
                    .matcher(sourceLines.get(i)).matches()) {
                endIndex = i;
            }
            if (startIndex != -1 && endIndex != -1
                    && startIndex + 1 < endIndex) {
                examples.add(parseSourceCodeExample(
                        sourceLines.subList(startIndex + 1, endIndex)));
                startIndex = -1;
                endIndex = -1;
            }
        }
        return examples;
    }

    private static SourceCodeExample parseSourceCodeExample(
            List<String> sourceLines) {
        String heading = parseValueFromPattern(sourceLines,
                SOURCE_CODE_EXAMPLE_HEADING_PATTERN, Function.identity(),
                () -> null);
        SourceType sourceType = parseValueFromPattern(sourceLines,
                SOURCE_CODE_EXAMPLE_TYPE_PATTERN, SourceType::valueOf,
                () -> SourceType.UNDEFINED);

        SourceCodeExample example = new SourceCodeExample();
        example.setHeading(heading);
        example.setSourceType(sourceType);
        example.setSourceCode(
                String.join("\n", trimWhitespaceAtStart(sourceLines)));
        return example;
    }

    private static <T> T parseValueFromPattern(List<String> sourceLines,
                                               Pattern pattern, Function<String, T> valueProvider,
                                               Supplier<T> nullValueProvider) {
        for (int i = 0; i < sourceLines.size(); i++) {
            Matcher matcher = pattern.matcher(sourceLines.get(i));
            if (matcher.matches()) {
                sourceLines.remove(i);
                return valueProvider.apply(matcher.group(1));
            }
        }
        return nullValueProvider.get();
    }

    private static List<String> trimWhitespaceAtStart(
            List<String> sourceLines) {
        int minIndent = Integer.MAX_VALUE;
        for (String line : sourceLines) {
            if (line == null || line.isEmpty()) {
                continue;
            }
            int indent = getWhitespaceCountAtStart(line);
            if (indent < minIndent) {
                minIndent = indent;
            }
        }
        List<String> trimmed = new ArrayList<>();
        for (String line : sourceLines) {
            if (line == null || line.isEmpty()) {
                trimmed.add("");
            } else {
                trimmed.add(line.substring(minIndent));
            }
        }
        return trimmed;
    }

    private static int getWhitespaceCountAtStart(String line) {
        int indent = 0;
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return indent;
            }
            indent++;
        }
        return indent;
    }
}
