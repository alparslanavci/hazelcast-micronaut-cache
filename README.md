# Table of Contents

* [Hazelcast Cache Integration for Micronaut](#hazelcast-cache-integration-for-micronaut)
* [Requirements and Installation](#requirements-and-installation)
* [Caching with Hazelcast in Micronaut](#caching-with-hazelcast-in-micronaut)


# Hazelcast Cache Integration for Micronaut

This repository contains Hazelcast Cache Integration for [Micronaut](http://micronaut.io/).
Similar to Spring and Grails, Micronaut provides a set of caching annotations. This integration contains an implementation of [SyncCache](https://docs.micronaut.io/latest/api/io/micronaut/cache/SyncCache.html) interface to provide caching features with Hazelcast.

# Requirements and Installation

- Maven

To install, clone the repository and run the following script in the root directory:

```
mvn clean install -DskipTests=true
```

# Caching with Hazelcast in Micronaut

If you wish to use Hazelcast to cache results then you need to have the `hazelcast-micronaut-cache` dependency on your classpath.

*Configuration with Maven:*

```
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-micronaut-cache</artifactId>
    <version>[VERSION]</version>
</dependency>
```

Then within your Micronaut application configuration, configure the Hazelcast cache as follows:

```
hazelcast:
    client: false
    xmlConfigPath: /path/to/hazelcast.xml
    caches:
        [YOUR_CACHE_NAME]:
            enabled: true
```

## Configuration properties

- **hazelcast.client:** (*default: `false`*) Set `true` for using Hazelcast Java client
- **hazelcast.xmlConfigPath:** The path of XML configuration file of HazelcastInstance. If you leave it empty, the XML configuration file in the classpath is used.
- **hazelcast.caches.YOUR_CACHE_NAME.enabled:** Set `true` to use the cache you configured. Replace the `YOUR_CACHE_NAME` with the cache name you would like to use.