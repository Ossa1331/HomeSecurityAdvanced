package com.example.homesecurity.entity;
import jakarta.persistence.*;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class DatabaseEntity {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    protected Long deviceId;

    @Column(name= "name")
    protected String deviceName;

    public DatabaseEntity(Long deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    public DatabaseEntity(){}

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    public static abstract class EntityBuilder<T extends DatabaseEntity, B extends EntityBuilder<T,B>>{

        protected T entity;

        protected abstract B self();

        public B deviceName(String deviceName){
            entity.deviceName=deviceName;
            return self();
        }
        public B deviceId(long deviceId){
            entity.deviceId=deviceId;
            return self();
        }
        public DatabaseEntity build(){
            return entity;
        }
    }
}
