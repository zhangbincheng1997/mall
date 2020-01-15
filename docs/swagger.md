# swagger
http://localhost:8080/swagger-ui.html
>* [swagger2markup](https://swagger2markup.github.io/swagger2markup): json -> asciidoc
>>* swagger2markup:convertSwagger2markup
>* [asciidoctor](https://asciidoctor.org/docs/asciidoctor-maven-plugin/) asciidoc -> html/pdf
>>* asciidoctor:process-asciidoc

```
    <pluginRepositories>
        <pluginRepository>
            <id>jcenter-snapshots</id>
            <name>jcenter</name>
            <url>http://oss.jfrog.org/artifactory/oss-snapshot-local/</url>
        </pluginRepository>
        <pluginRepository>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>jcenter-releases</id>
            <name>jcenter</name>
            <url>http://jcenter.bintray.com</url>
        </pluginRepository>
    </pluginRepositories>

    <build>
        <plugins>
            <plugin>
                <groupId>io.github.swagger2markup</groupId>
                <artifactId>swagger2markup-maven-plugin</artifactId>
                <version>1.3.3</version>
                <configuration>
                    <swaggerInput>http://localhost:8080/v2/api-docs</swaggerInput>
                    <outputFile>src/docs/asciidoc/generated/all</outputFile>
                    <config>
                        <swagger2markup.markupLanguage>ASCIIDOC</swagger2markup.markupLanguage>
                        <swagger2markup.outputLanguage>ZH</swagger2markup.outputLanguage>
                        <swagger2markup.pathsGroupedBy>TAGS</swagger2markup.pathsGroupedBy>
                        <swagger2markup.generatedExamplesEnabled>true</swagger2markup.generatedExamplesEnabled>
                        <swagger2markup.inlineSchemaEnabled>false</swagger2markup.inlineSchemaEnabled>
                    </config>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.asciidoctor</groupId>
                <artifactId>asciidoctor-maven-plugin</artifactId>
                <version>1.5.6</version>
                <configuration>
                    <sourceDirectory>src/docs/asciidoc/generated</sourceDirectory>
                    <outputDirectory>src/docs/asciidoc/html</outputDirectory>
                    <backend>html</backend>
                    <sourceHighlighter>coderay</sourceHighlighter>
                    <attributes>
                        <toc>left</toc>
                        <toclevels>3</toclevels>
                        <sectnums>true</sectnums>
                    </attributes>
                </configuration>
            </plugin>
        </plugins>
    </build>
```