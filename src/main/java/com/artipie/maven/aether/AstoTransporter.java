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

import com.artipie.asto.Key;
import com.artipie.asto.blocking.BlockingStorage;
import org.apache.commons.io.IOUtils;
import org.eclipse.aether.spi.connector.transport.AbstractTransporter;
import org.eclipse.aether.spi.connector.transport.GetTask;
import org.eclipse.aether.spi.connector.transport.PeekTask;
import org.eclipse.aether.spi.connector.transport.PutTask;
import org.eclipse.aether.spi.connector.transport.TransportTask;
import org.eclipse.aether.spi.connector.transport.Transporter;

/**
 * Adapts Asto to {@link Transporter}.
 * @since 0.1
 */
public final class AstoTransporter extends AbstractTransporter {

    /**
     * Asto.
     */
    private final BlockingStorage asto;

    /**
     * All args constructor.
     * @param asto Asto
     */
    public AstoTransporter(final BlockingStorage asto) {
        this.asto = asto;
    }

    @Override
    public int classify(final Throwable error) {
        int code = Transporter.ERROR_OTHER;
        if (error instanceof ResourceNotFoundException) {
            code = Transporter.ERROR_NOT_FOUND;
        }
        return code;
    }

    @Override
    public void implPeek(final PeekTask task) throws Exception {
        final Key key = new TaskKey(task).key();
        if (!this.asto.exists(key)) {
            throw new ResourceNotFoundException(
                String.format(
                    "Resource does not exist in Asto: key %s and location %s",
                    key,
                    task.getLocation()
                )
            );
        }
    }

    @Override
    public void implGet(final GetTask task) throws Exception {
        try (var write = task.newOutputStream()) {
            IOUtils.write(this.asto.value(new TaskKey(task).key()), write);
        }
    }

    @Override
    public void implPut(final PutTask task) throws Exception {
        try (var read = task.newInputStream()) {
            this.asto.save(new TaskKey(task).key(), IOUtils.toByteArray(read));
        }
    }

    @Override
    public void implClose() {
        // noop
    }

    /**
     * Maps TransportTask location to Asto key.
     * @since 0.1
     */
    private final class TaskKey {

        /**
         * Transport task.
         */
        private final TransportTask task;

        /**
         * All args constructor.
         * @param task Transport task
         */
        private TaskKey(final TransportTask task) {
            this.task = task;
        }

        /**
         * Maps TransportTask location to Asto key.
         * @return Task location as Asto key.
         */
        private Key key() {
            return new Key.From(this.task.getLocation().getPath());
        }
    }
}