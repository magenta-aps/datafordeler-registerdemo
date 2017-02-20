package dk.magenta.datafordeler.registerdemo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.StringJoiner;
import java.util.UUID;

/**
 * Created by lars on 15-02-17.
 */
public class Event {

    public Event() {
        this.beskedID = UUID.randomUUID().toString();
    }

    public class MessageData {

        @JsonProperty(value = "Objektdata")
        private ObjectData objektData;

        @JsonProperty(value = "Objektreference")
        private ObjectReference objektReference;

        @JsonIgnore
        public String getDataskema() {
            if (this.objektData != null) {
                return this.objektData.getDataskema();
            }
            return null;
        }

        public void setDataskema(String dataskema) {
            if (this.objektData == null) {
                this.objektData = new ObjectData();
            }
            this.objektData.setDataskema(dataskema);
        }

        @JsonIgnore
        public String getObjektData() {
            if (this.objektData != null) {
                return this.objektData.getObjektData();
            }
            return null;
        }

        public void setObjektData(String objektData) {
            if (this.objektData == null) {
                this.objektData = new ObjectData();
            }
            this.objektData.setObjektData(objektData);
        }

        @JsonIgnore
        public String getObjektReference() {
            if (this.objektReference != null) {
                return this.objektReference.getObjektReference();
            }
            return null;
        }

        public void setObjektReference(String objektReference) {
            if (this.objektReference == null) {
                this.objektReference = new ObjectReference();
            }
            this.objektReference.setObjektReference(objektReference);
        }
    }

    public class ObjectData {

        @JsonProperty
        private String dataskema;

        @JsonProperty(value = "objektdata")
        private String objektData;

        public String getDataskema() {
            return dataskema;
        }

        public void setDataskema(String dataskema) {
            this.dataskema = dataskema;
        }

        public String getObjektData() {
            return objektData;
        }

        public void setObjektData(String objektData) {
            this.objektData = objektData;
        }
    }

    public class ObjectReference {

        @JsonProperty
        private String objektReference;

        public String getObjektReference() {
            return objektReference;
        }

        public void setObjektReference(String objektReference) {
            this.objektReference = objektReference;
        }
    }

    @JsonProperty
    private String beskedID;

    @JsonProperty
    private String beskedVersion;

    @JsonProperty(value="beskedData")
    private MessageData messageData;

    public String getBeskedID() {
        return beskedID;
    }

    public void setBeskedID(String beskedID) {
        this.beskedID = beskedID;
    }

    public String getBeskedVersion() {
        return beskedVersion;
    }

    public void setBeskedVersion(String beskedVersion) {
        this.beskedVersion = beskedVersion;
    }

    private MessageData ensureMessageData() {
        if (this.messageData == null) {
            this.messageData = new MessageData();
        }
        return this.messageData;
    }

    @JsonIgnore
    public String getDataskema() {
        if (this.messageData != null) {
            return this.messageData.getDataskema();
        }
        return null;
    }

    public void setDataskema(String dataskema) {
        this.ensureMessageData().setDataskema(dataskema);
    }

    @JsonIgnore
    public String getObjektData() {
        if (this.messageData != null) {
            return this.messageData.getObjektData();
        }
        return null;
    }

    public void setObjektData(String objektData) {
        this.ensureMessageData().setObjektData(objektData);
    }

    @JsonIgnore
    public String getObjectReference() {
        if (this.messageData != null) {
            return this.messageData.getObjektReference();
        }
        return null;
    }

    public void setObjektReference(String objektReference) {
        this.ensureMessageData().setObjektReference(objektReference);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event(");
        StringJoiner joiner = new StringJoiner(", ");
        joiner.add("beskedID: "+this.getBeskedID());
        joiner.add("beskedVersion: "+this.getBeskedVersion());
        joiner.add("dataskema: "+this.getDataskema());
        joiner.add("objektData: "+this.getObjektData());
        joiner.add("objektReference: "+this.getObjectReference());
        sb.append(joiner.toString());
        sb.append(")");
        return sb.toString();
    }
}
