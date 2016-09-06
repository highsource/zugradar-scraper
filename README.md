# Zugradar Scraper

Scrapes [Zugradar](https://www.bahn.de/p/view/service/auskunft/zugradar.shtml) for interesting information.

# Legal warning

The code is provided for educational purposes only.

Actual usage of this code to scrape any data would contradict [terms and conditions of bahn.de](https://www.bahn.de/p/view/home/agb/nutzungsbedingungen.shtml?dbkanal_007=L01_S01_D001_KIN0001_footer-nutzungsbedingungen_LZ01).

# Usage

## Getting train numbers

For most operations you'll need a mapping of the train number to internal train id.

```
java -cp zugradarscraper.jar org.hisrc.zugradarscraper.application.TrainIdScraper -s 2015-12-13 -e 2016-12-11
```

This produces `trainIds.csv`:

```
number,classification,id
477,EN,84/196395/18/19/80
464,EN,84/196319/18/19/80
463,EN,84/196309/18/19/80
452,EN,84/194278/18/19/80
120,ICE,84/192827/18/19/80
...
```

# License

The code is provided under [MIT License](https://github.com/highsource/zugradar-scraper/blob/master/LICENSE).