/*
 * MIT License
 *
 * Copyright (c) 2020 Artipie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.artipie.maven.aether;

/**
 * Represents {@link org.eclipse.aether.spi.locator.ServiceLocator} misconfiguration.
 * The given service cannot be located or initialized.
 * @since 0.1
 * @deprecated Outdated due architectural changes in 0.2
 */
@Deprecated
public class ServiceLocatorException extends IllegalStateException {

    /**
     * Constructs a new exception instance with no cause.
     * Internally it builds its detail message incorporating given service type.
     * @param service Service type
     */
    public ServiceLocatorException(final Class<?> service) {
        this(service, null);
    }

    /**
     * Constructs a new exception instance.
     * Internally it builds its detail message incorporating given service type.
     * @param service Service type
     * @param cause The exception cause
     */
    public ServiceLocatorException(final Class<?> service, final Throwable cause) {
        super(String.format("Cannot locate or initialize service %s", service), cause);
    }
}
