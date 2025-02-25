package com.savoir.apache.karaf.ai.gateway.ollama.tool.plugin;

/*
 * Copyright (c) 2012-2024 Savoir Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.savoir.apache.karaf.ai.gateway.spi.ExecutorPlugin;

import com.savoir.existing.service.api.ExistingService;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import java.time.Duration;

public class OllamaToolEnabledPlugin implements ExecutorPlugin {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    Assistant assistant;
    ChatLanguageModel model;
    String modelName;
    String baseUrl;
    ExistingService existingService;

//----------------------------------------------------------------------------------------------------------------------
// LangChain4j Tool Facade
//----------------------------------------------------------------------------------------------------------------------

    @Description("Available tools to AI model to perform user requests")
    class UseOurServicesToolFacade {

        @Tool("Converts file referenced by fileId to the new format.")
        String convertToNewFormat(String fileId) {
            System.out.println("Converting fileId to the new format: " + fileId);
            return existingService.convertToNewSchema(fileId);
        }

        @Tool("Send quoted message body to destination with postfix queue")
        String sendEventToQueue(String quote, String destination) {
            System.out.println("Send message \"" + quote + "\"  to destination: " + destination);
            //anotherExistingService.sendJMS(message, destination)
            return "Sent message \"" + quote + "\" to destination: " + destination;
        }

        @Tool("Not a tool request")
        void noOp(String input) {
            //
        }

    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public OllamaToolEnabledPlugin(String modelName, String baseUrl, ExistingService existingService) {
        this.modelName = modelName;
        this.baseUrl = baseUrl;
        this.existingService = existingService;

        model = OllamaChatModel.builder()
                .baseUrl(this.baseUrl)
                .modelName(this.modelName)
                .temperature(0.8)
                .timeout(Duration.ofSeconds(60))
                .build();

        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .tools(new UseOurServicesToolFacade())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

//----------------------------------------------------------------------------------------------------------------------
// Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public String describe() {
        return "This is an Ollama plugin that utlizies tools on our system.";
    }

    @Override
    public String generate(String prompt) {
        return assistant.chat(prompt);
    }
}
