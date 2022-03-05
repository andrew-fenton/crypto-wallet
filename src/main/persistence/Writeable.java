package persistence;

import org.json.JSONObject;

// Used code from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writeable {

    // EFFECTS: returns this as a JSON object
    JSONObject toJson();

}
