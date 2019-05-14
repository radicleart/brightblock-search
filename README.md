# Brightblock Search

Search microservice providing a Lucene index of decentralised application and user data via [Blockstack](https://blockstack.org) infrastructure. 

## Table of Contents

- [Purpose](#purpose)
- [Privacy](#privacy)
- [Configuration](#configuration)
- [Domains](#domains)
- [Index API](#index-api)
- [Search API](#search-api)
- [Roadmap](#roadmap)

## Purpose

Provides customisable search index of user data for decentralised applications. Marketplaces and auctions are 
key examples where specialised search results and filters form the backbone to the rest of the platforms functionality.
This component provides a way of indexing decentralised user data in an open and transparent way that allows fair
competition between d-apps and users to evolve. 

The indexer starts from a decentralised blockstack user identity. It pulls the apps the user has visited and 
indexes data whose app domain matches one of the domains in the yaml configuration of the indexer.

The indexer can be forked from [open source repository](https://github.com/mjoecohen/brightblock-search). Or can be
used via;

1. shared hosting model - add new d-app domain in configuration,
2. siloed hosting - pull and run with docker.

## Privacy

The indexer can only access unencrypted user data which is publicly visible by default. The intention is to only index data in specific
fields such as 'title', 'description', 'keywords' and possibly some name/value pairs to enable some advanced range search and filtering. 
It's hoped the community can help standardise both the formats and the allowed fields in such a way that this develops 
transparently to end users going forward - for example standardising end points that enable users to removed their data from index in a straightforward manner.

## Configuration

The data to index is configured via yaml. For example the following chunk of yaml;
```spring:
    profiles: staging
application:
   blockstackBase: https://core-staging.brightblock.org
   domains:
      -  domain: localhost
         indexFiles: 
            - indexFileName: records_v01.json
              indexObjType: artwork
            - indexFileName: auctions_v01.json
              indexObjType: auction
         fields:
            - id
            - title
            - description
            - keywords
      -  domain: www.brightblock.org
         indexFiles: 
            - indexFileName: records_v01.json
              indexObjType: artwork
            - indexFileName: auctions_v01.json
              indexObjType: auction
         fields:
            - id
            - title
            - description
            - keywords
```
will attempt to index data stored under two domains 'localhost' and 'www.brightblock.org'. On finding users who have visited these 
domains it will read from gaia storage the files;
- records_v01.json
- auctions_v01.json

and within these files it will expect to find an array of json objects at the root of the file in format;

```
records [
	{ id: 'some id', title: 'some title', description: 'some description' ...
]
```

ignoring any other content. This data will then be indexed in a lucene index with the id as a key along with the domain and object type.

The config value for parameter 'blockstackBase' indicates the blockstack node to ask for user data from. In the hosted version this is a 
blockstack node running on the same server as the search micro-service.

## Index API

### Clear Index

> value = "/index/dapps/clear", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }

Clears the index out - temporary method for developemnt and test.

### Index Names

> value = "/index/pages/{from}/{to}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }

Index blockstack names between the pages indicated calls pulls names from [Blockstack API](https://core.blockstack.org/) and then
updates the index for domain matches between user apps and domains in the yaml config.

> value = "/index/users/{names}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }

Index blockstack names - comma separated list.

### Add / Remove Search Documents

> value = "/index/addRecord", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }

Add the given indexable object to the index. Post data:

```
{
	title: '',
	description: '',
	keywords: '',
	owner: '', // blockstack id of the owner of the record
	objType: '...',  // in art project these are one of ['auction', 'artwork'] for specific searchs
	domain: '...',  // the domain of the d-app
}
```

> value = "/index/removeRecord/{field}/{value}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }

Remove the given record from the index.

```
field = 'id'
value = 'value of unique identifier'
```

Note: this needs more information as single identifier can't be relied on to locate a record uniquely with multiple domains.


## Search API

> value = "/index/names/fetch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }

Fetches all user names so far added to the index - useful for test and dev.

> value = "/index/dapps/fetch", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }

Fetches all d-app records so far added to the index - useful for test and dev.

> value = "/index/names/query/{field}?q=query_string", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }

Query the names index. For example if field='apps' and query_string='your domain' this will return the users who have logged in to
your domain.

> value = "/index/dapps/{domain}/{objType}/{field}?q=query_string", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }

Query the dapps index. For example if field='title' and query_string='hallo sailor' this will return all records of type 'objType' 
under domain 'domain' whose title contains the full text indexed search words.

## Domains

### Add Blockstack Id(s) to Index

## Examples

### Add Blockstack Id(s) to Index

> https://search.brightblock.org/index/users/mike.personal.id,brightblock.id

```
{
"failed": false,
"timestamp": 1542796896542,
"httpStatus": "OK",
"details": "Indexing users: [mike.personal.id, brightblock.id] in background.",
"message": "Success!"
}
```


> https://search.brightblock.org/index/dapps/radicle.art/artwork/description?q=capitals

```
{
"failed": false,
"timestamp": 1542727510888,
"httpStatus": "OK",
"details": [
{
"id": "1540550024050",
"title": "Living in the alley",
"description": "Block capitals on brick",
"owner": "mike.personal.id",
"objType": "artwork",
"domain": "radicle.art",
"keywords": "Photography,Illustration.3D,2D,Film & Video,Mix-media"
}
],
"message": "Success!"
}
```

## Roadmap

This is an early stage prototype and is waiting on funding for further development.


