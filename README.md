# Posmulten

[![Build Status](https://travis-ci.org/starnowski/posmulten.svg?branch=master)](https://travis-ci.org/starnowski/posmulten)
[![Download](https://api.bintray.com/packages/starnowski/posmulten/posmulten/images/download.svg) ](https://bintray.com/starnowski/posmulten/posmulten/_latestVersion)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.starnowski.posmulten/postgresql-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.starnowski.posmulten%22%20AND%20a:%22postgresql-core%22)


* [Introduction](#introduction)
* [Multi-tenancy](#multi-tenancy)


# Introduction
Posmulten library is an open-source project for the generation of SQL DDL statements that make it easy for implementation of Shared Schema Multi-tenancy strategy via the [Row Security Policies](https://www.postgresql.org/docs/9.6/ddl-rowsecurity.html) in the Postgres database.

# Multi-tenancy

Based on [hibernate documentation](https://docs.jboss.org/hibernate/orm/4.3/devguide/en-US/html/ch16.html) 
_`the term multi-tenancy in general is applied to software development to indicate an architecture in which a single running instance of an application simultaneously serves multiple clients (tenants).`_
There three main strategies [separate database](#separate-database), [separate schema](#separate-schema) and [shared schema](#shared-schema)

# TODO Mention about three main strategies

## Separate database
TODO

## Separate schema
TODO

## Shared schema
TODO

# TODO Concept What is shared schema strategy
# TODO UML
# TODO How sql query looks like for shared schema
# TODO How Posmulten is doing this?
# TODO  - RLS policy
# TODO  - Constraints
# TODO  - Connection settings 

