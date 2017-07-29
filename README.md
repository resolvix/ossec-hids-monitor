# ossec-hids-monitor

OSSEC HIDS Monitor ("OHM") is an application designed to monitor, and process
security alerts logged to a centralised repository by [OSSEC HIDS](https://ossec.github.io/),
a popular open source host intrusion detection system.

OHM is structured in terms of an application mainframe that is responsible
for monitoring the OSSEC HIDS alert repository, and one or more modules that
may be enabled to process the alerts in accordance with business
requirements.

## Command line options

`help` (alternatively, `-h`) displays help information for the application
mainframe, and the available modules.

`list` (alternatively, `-l`) displays the list of available modules.

`config <file>.conf` (alternatively, `-c <file>.conf`) specifies the pathname
for the configuration file.

`from <date>` (alternatively, `-f <date>`) specifies the start of the period
over which OHM should process alerts from the OSSEC HIDS alert repository.
`<date>` may be specified as an ISO-8601 date of the form `YYYY-MM-DD`, or
in the following relative terms: `today`, or `tomorrow`. The `from` date is
taken to refer to the start of the relevant day in the relevant time zone.

`to <date>` (alternatively, `-t <date>`) specifies the end of the period over
which OHM should process alerts from the OSSEC HIDS alert repository. `<date>`
may be specified as an ISO-8601 date of the form `YYYY-MM-DD`, or in the
following relative terms: `now`. The `to` date is taken to refer to the end
of the relevant day in the relevant time zone or, where the date specified is
the present day, the time "now".

## Environment variables

`OHM_HOME`

`OHM_CONFIG_PATHNAME`

## Configuration

OHM is configured using a JSON file of the form, where references to JSON
values in the form `<...>` are references to the data dictionary, provided
in the subsection below --

```
{

    "alertSourceURI": <alert-source-uri>,

    "alertThreshold": <alert-threshold>,

}
```
### Data dictionary

`<alert-source-uri>`

`<alert-threshold>`

