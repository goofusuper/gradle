/*
 * Copyright 2017 the original author or authors.
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

package org.gradle.api.internal.tasks;

import org.gradle.api.JavaVersion;
import org.gradle.api.internal.tasks.compile.JavaCompileSpec;
import org.gradle.api.internal.tasks.compile.JavaCompilerFactory;
import org.gradle.jvm.platform.JavaPlatform;
import org.gradle.language.base.internal.compile.CompileSpec;
import org.gradle.language.base.internal.compile.Compiler;
import org.gradle.platform.base.internal.toolchain.ToolProvider;
import org.gradle.process.internal.ExecActionFactory;

public class JavacCommandLineToolchain extends AbstractJavaToolChain {
    public JavacCommandLineToolchain(JavaCompilerFactory compilerFactory, ExecActionFactory execActionFactory) {
        super(compilerFactory, execActionFactory);
    }

    @Override
    public JavaVersion getJavaVersion() {
        // We do not know the Java version
        return JavaVersion.current();
    }

    @Override
    public ToolProvider select(JavaPlatform targetPlatform) {
        return new JavaToolProvider() {
            @Override
            public <T extends CompileSpec> Compiler<T> newCompiler(Class<T> spec) {
                if (JavaCompileSpec.class.isAssignableFrom(spec)) {
                    @SuppressWarnings("unchecked") Compiler<T> compiler = (Compiler<T>) compilerFactory.create(spec);
                    return compiler;
                }

                throw new IllegalArgumentException(String.format("Don't know how to compile using spec of type %s.", spec.getClass().getSimpleName()));
            }
        };
    }
}
