/* ###
 * IP: GHIDRA
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
package ghidra.app.plugin.core.debug.gui.thread;

import java.util.function.BiConsumer;
import java.util.function.Function;

import docking.widgets.table.DefaultEnumeratedColumnTableModel.EnumeratedTableColumn;
import ghidra.trace.model.Lifespan;

public enum ThreadTableColumns implements EnumeratedTableColumn<ThreadTableColumns, ThreadRow> {
	NAME("Name", String.class, ThreadRow::getName, ThreadRow::setName, true),
	CREATED("Created", Long.class, ThreadRow::getCreationSnap, true),
	DESTROYED("Destroyed", String.class, ThreadRow::getDestructionSnap, true),
	STATE("State", ThreadState.class, ThreadRow::getState, true),
	COMMENT("Comment", String.class, ThreadRow::getComment, ThreadRow::setComment, true),
	PLOT("Plot", Lifespan.class, ThreadRow::getLifespan, false);

	private final String header;
	private final Function<ThreadRow, ?> getter;
	private final BiConsumer<ThreadRow, Object> setter;
	private final boolean sortable;
	private final Class<?> cls;

	<T> ThreadTableColumns(String header, Class<T> cls, Function<ThreadRow, T> getter,
			boolean sortable) {
		this(header, cls, getter, null, sortable);
	}

	@SuppressWarnings("unchecked")
	<T> ThreadTableColumns(String header, Class<T> cls, Function<ThreadRow, T> getter,
			BiConsumer<ThreadRow, T> setter, boolean sortable) {
		this.header = header;
		this.cls = cls;
		this.getter = getter;
		this.setter = (BiConsumer<ThreadRow, Object>) setter;
		this.sortable = sortable;
	}

	@Override
	public String getHeader() {
		return header;
	}

	@Override
	public Class<?> getValueClass() {
		return cls;
	}

	@Override
	public Object getValueOf(ThreadRow row) {
		return getter.apply(row);
	}

	@Override
	public boolean isEditable(ThreadRow row) {
		return setter != null;
	}

	@Override
	public boolean isSortable() {
		return sortable;
	}

	@Override
	public void setValueOf(ThreadRow row, Object value) {
		setter.accept(row, value);
	}
}
