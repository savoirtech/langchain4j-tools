# langchain4j-tools

Demo showing LangChain4j tools annotation.

This demo provides an Ollama plugin which will utilize LangChain4j Tool support to
call existing service available in the runtime Karaf. 

This existing service may be a facade to a RESTFul service, or similar.

# Prerequisites

Installation of API-Gateway demo on Apache Karaf.
See: https://github.com/savoirtech/apache-karaf-langchain4j-demo/tree/ai-gateway

# Build

```text
mvn clean install
```

# Install

```text
install -s mvn:com.savoir/existingService
install -s mvn:com.savoir/ollamaPlugin
```

# Reaching Out

Please do not hesitate to reach out with questions and comments, here on
the Blog, or through the Savoir Technologies website at
<https://www.savoirtech.com>.

# With Thanks

Thank you to the Apache Karaf, and LangChain4J communities.

\(c\) 2025 Savoir Technologies
