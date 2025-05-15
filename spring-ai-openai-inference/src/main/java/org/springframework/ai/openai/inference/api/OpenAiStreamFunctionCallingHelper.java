/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ai.openai.inference.api;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to support Streaming function calling.
 *
 * It can merge the streamed ChatCompletionChunk in case of function calling message.
 *
 * @author Christian Tzolov
 * @author Thomas Vitale
 * @author Alexandros Pappas
 * @since 0.8.1
 */
public class OpenAiStreamFunctionCallingHelper {

	/**
	 * Merge the previous and current ChatCompletionChunk into a single one.
	 * @param previous the previous ChatCompletionChunk
	 * @param current the current ChatCompletionChunk
	 * @return the merged ChatCompletionChunk
	 */
	public OpenAiApi.ChatCompletionChunk merge(OpenAiApi.ChatCompletionChunk previous, OpenAiApi.ChatCompletionChunk current) {

		if (previous == null) {
			return current;
		}

		String id = (current.id() != null ? current.id() : previous.id());
		Long created = (current.created() != null ? current.created() : previous.created());
		String model = (current.model() != null ? current.model() : previous.model());
		String serviceTier = (current.serviceTier() != null ? current.serviceTier() : previous.serviceTier());
		String systemFingerprint = (current.systemFingerprint() != null ? current.systemFingerprint()
				: previous.systemFingerprint());
		String object = (current.object() != null ? current.object() : previous.object());
		OpenAiApi.Usage usage = (current.usage() != null ? current.usage() : previous.usage());

		OpenAiApi.ChatCompletionChunk.ChunkChoice previousChoice0 = (CollectionUtils.isEmpty(previous.choices()) ? null : previous.choices().get(0));
		OpenAiApi.ChatCompletionChunk.ChunkChoice currentChoice0 = (CollectionUtils.isEmpty(current.choices()) ? null : current.choices().get(0));

		OpenAiApi.ChatCompletionChunk.ChunkChoice choice = merge(previousChoice0, currentChoice0);
		List<OpenAiApi.ChatCompletionChunk.ChunkChoice> chunkChoices = choice == null ? List.of() : List.of(choice);
		return new OpenAiApi.ChatCompletionChunk(id, chunkChoices, created, model, serviceTier, systemFingerprint, object, usage);
	}

	private OpenAiApi.ChatCompletionChunk.ChunkChoice merge(OpenAiApi.ChatCompletionChunk.ChunkChoice previous, OpenAiApi.ChatCompletionChunk.ChunkChoice current) {
		if (previous == null) {
			return current;
		}

		OpenAiApi.ChatCompletionFinishReason finishReason = (current.finishReason() != null ? current.finishReason()
				: previous.finishReason());
		Integer index = (current.index() != null ? current.index() : previous.index());

		OpenAiApi.ChatCompletionMessage message = merge(previous.delta(), current.delta());

		OpenAiApi.LogProbs logprobs = (current.logprobs() != null ? current.logprobs() : previous.logprobs());
		return new OpenAiApi.ChatCompletionChunk.ChunkChoice(finishReason, index, message, logprobs);
	}

	private OpenAiApi.ChatCompletionMessage merge(OpenAiApi.ChatCompletionMessage previous, OpenAiApi.ChatCompletionMessage current) {
		String content = (current.content() != null ? current.content()
				: "" + ((previous.content() != null) ? previous.content() : ""));
		OpenAiApi.ChatCompletionMessage.Role role = (current.role() != null ? current.role() : previous.role());
		role = (role != null ? role : OpenAiApi.ChatCompletionMessage.Role.ASSISTANT); // default to ASSISTANT (if null
		String name = (current.name() != null ? current.name() : previous.name());
		String toolCallId = (current.toolCallId() != null ? current.toolCallId() : previous.toolCallId());
		String refusal = (current.refusal() != null ? current.refusal() : previous.refusal());
		OpenAiApi.ChatCompletionMessage.AudioOutput audioOutput = (current.audioOutput() != null ? current.audioOutput()
				: previous.audioOutput());
		List<OpenAiApi.ChatCompletionMessage.Annotation> annotations = (current.annotations() != null ? current.annotations()
				: previous.annotations());

		List<OpenAiApi.ChatCompletionMessage.ToolCall> toolCalls = new ArrayList<>();
		OpenAiApi.ChatCompletionMessage.ToolCall lastPreviousTooCall = null;
		if (previous.toolCalls() != null) {
			lastPreviousTooCall = previous.toolCalls().get(previous.toolCalls().size() - 1);
			if (previous.toolCalls().size() > 1) {
				toolCalls.addAll(previous.toolCalls().subList(0, previous.toolCalls().size() - 1));
			}
		}
		if (current.toolCalls() != null) {
			if (current.toolCalls().size() > 1) {
				throw new IllegalStateException("Currently only one tool call is supported per message!");
			}
			var currentToolCall = current.toolCalls().iterator().next();
			if (StringUtils.hasText(currentToolCall.id())) {
				if (lastPreviousTooCall != null) {
					toolCalls.add(lastPreviousTooCall);
				}
				toolCalls.add(currentToolCall);
			}
			else {
				toolCalls.add(merge(lastPreviousTooCall, currentToolCall));
			}
		}
		else {
			if (lastPreviousTooCall != null) {
				toolCalls.add(lastPreviousTooCall);
			}
		}
		return new OpenAiApi.ChatCompletionMessage(content, role, name, toolCallId, toolCalls, refusal, audioOutput, annotations);
	}

	private OpenAiApi.ChatCompletionMessage.ToolCall merge(OpenAiApi.ChatCompletionMessage.ToolCall previous, OpenAiApi.ChatCompletionMessage.ToolCall current) {
		if (previous == null) {
			return current;
		}
		String id = (StringUtils.hasText(current.id()) ? current.id() : previous.id());
		String type = (current.type() != null ? current.type() : previous.type());
		OpenAiApi.ChatCompletionMessage.ChatCompletionFunction function = merge(previous.function(), current.function());
		return new OpenAiApi.ChatCompletionMessage.ToolCall(id, type, function);
	}

	private OpenAiApi.ChatCompletionMessage.ChatCompletionFunction merge(OpenAiApi.ChatCompletionMessage.ChatCompletionFunction previous, OpenAiApi.ChatCompletionMessage.ChatCompletionFunction current) {
		if (previous == null) {
			return current;
		}
		String name = (StringUtils.hasText(current.name()) ? current.name() : previous.name());
		StringBuilder arguments = new StringBuilder();
		if (previous.arguments() != null) {
			arguments.append(previous.arguments());
		}
		if (current.arguments() != null) {
			arguments.append(current.arguments());
		}
		return new OpenAiApi.ChatCompletionMessage.ChatCompletionFunction(name, arguments.toString());
	}

	/**
	 * @param chatCompletion the ChatCompletionChunk to check
	 * @return true if the ChatCompletionChunk is a streaming tool function call.
	 */
	public boolean isStreamingToolFunctionCall(OpenAiApi.ChatCompletionChunk chatCompletion) {

		if (chatCompletion == null || CollectionUtils.isEmpty(chatCompletion.choices())) {
			return false;
		}

		var choice = chatCompletion.choices().get(0);
		if (choice == null || choice.delta() == null) {
			return false;
		}
		return !CollectionUtils.isEmpty(choice.delta().toolCalls());
	}

	/**
	 * @param chatCompletion the ChatCompletionChunk to check
	 * @return true if the ChatCompletionChunk is a streaming tool function call and it is
	 * the last one.
	 */
	public boolean isStreamingToolFunctionCallFinish(OpenAiApi.ChatCompletionChunk chatCompletion) {

		if (chatCompletion == null || CollectionUtils.isEmpty(chatCompletion.choices())) {
			return false;
		}

		var choice = chatCompletion.choices().get(0);
		if (choice == null || choice.delta() == null) {
			return false;
		}
		return choice.finishReason() == OpenAiApi.ChatCompletionFinishReason.TOOL_CALLS;
	}

	/**
	 * Convert the ChatCompletionChunk into a ChatCompletion. The Usage is set to null.
	 * @param chunk the ChatCompletionChunk to convert
	 * @return the ChatCompletion
	 */
	public OpenAiApi.ChatCompletion chunkToChatCompletion(OpenAiApi.ChatCompletionChunk chunk) {
		List<OpenAiApi.ChatCompletion.Choice> choices = chunk.choices()
			.stream()
			.map(chunkChoice -> new OpenAiApi.ChatCompletion.Choice(chunkChoice.finishReason(), chunkChoice.index(), chunkChoice.delta(),
					chunkChoice.logprobs()))
			.toList();

		return new OpenAiApi.ChatCompletion(chunk.id(), choices, chunk.created(), chunk.model(), chunk.serviceTier(),
				chunk.systemFingerprint(), "chat.completion", null);
	}

}
// ---
