package com.bz.agent.test;

import io.micrometer.common.KeyValues;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;
import org.springframework.ai.model.tool.DefaultToolCallingManager;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.tool.observation.DefaultToolCallingObservationConvention;
import org.springframework.ai.tool.observation.ToolCallingObservationContext;
import org.springframework.ai.tool.observation.ToolCallingObservationConvention;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/18
 */
public class TestToolCallingManager {
    public static void main(String[] args) {

        DefaultToolCallingManager callingManager = ToolCallingManager.builder().build();

        callingManager.setObservationConvention(new DefaultToolCallingObservationConvention());
    }

    public class Customer implements ObservationConvention {


        @Override
        public KeyValues getLowCardinalityKeyValues(Observation.Context context) {
            return ObservationConvention.super.getLowCardinalityKeyValues(context);
        }

        @Override
        public KeyValues getHighCardinalityKeyValues(Observation.Context context) {
            return ObservationConvention.super.getHighCardinalityKeyValues(context);
        }

        @Override
        public boolean supportsContext(Observation.Context context) {
            return false;
        }

        @Override
        public String getName() {
            return ObservationConvention.super.getName();
        }

        @Override
        public String getContextualName(Observation.Context context) {
            return ObservationConvention.super.getContextualName(context);
        }
    }
}
