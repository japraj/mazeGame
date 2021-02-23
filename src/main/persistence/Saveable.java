package persistence;

import org.json.JSONObject;

// An entity that can be saved in JSON format
// CITATION: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Saveable {
    // EFFECTS: produces a JSON representation of this
    JSONObject toJSON();
}
