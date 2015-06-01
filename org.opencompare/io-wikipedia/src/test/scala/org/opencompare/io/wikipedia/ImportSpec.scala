package org.opencompare.io.wikipedia

import java.io.{File, FileWriter}
import java.util.concurrent.Executors

import org.opencompare.api.java
import org.opencompare.api.java.{PCMFactory, AbstractFeature, Cell, PCM}
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import org.opencompare.api.java.io.{CSVLoader, CSVExporter}
import org.opencompare.api.java.util.{SimplePCMElementComparator, PCMElementComparator}
import org.opencompare.io.wikipedia.export.{WikiTextExporter, PCMModelExporter}
import org.opencompare.io.wikipedia.export.{WikiTextExporter, PCMModelExporter}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent._
import scala.io.Source

/**
 * Created by smangin on 01/06/15.
 */
class ImportSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(20))
  val miner = new WikipediaPageMiner
  val pcmFactory = new PCMFactoryImpl
  val pcmExporter = new PCMModelExporter
  val csvExporter = new CSVExporter
  val wikiTextExporter = new WikiTextExporter
  val csvLoader = new CSVLoader(new PCMFactoryImpl, ',')

  val title = "Comparison (grammar)"
  val csv = """"Country code,Country,Speed limit on motorway (km/h),Speed limit on dual carriageway (km/h),Speed limit on single carriageway (km/h),Speed limit in urban area,Permitted alcohol level (%),Toll roads,Seatbelt required,Minimum child age (front seat),Triangle required,First aid required,Fire extinguisher required,Spare bulb required,Minimum driver's age,Tow rope required
A,Austria,130,100,100,50,0.05,Yes,All,N/A,Yes,Yes,Recommended,No,17,Yes
AL,Albania,110,N/A,90,40,0,,N/A,N/A,Recommended,Recommended,Recommended,No,N/A,Yes
AND,Andorra,N/A,N/A,90,50,0.05,,Front,10,Yes,Recommended,Recommended,No,18,Yes
B,Belgium,120,120,90,50,0.05,No[1],All,12,Yes,Yes,Yes,No,18 (17 with supervision of parents),Yes
BG,Bulgaria,140,N/A,90,50,0.05,Yes,All,10,Yes,Yes,Yes,No,18,Yes
BIH,Bosnia and Herzegovina,130,100,80,50,0.03,,Front,12,Yes,Yes,Recommended,Yes,18,Yes
BY,Belarus,110,N/A,90,60,<0.03,Yes,Front,12,Yes,Yes,Yes,No,18,Yes
CH,Switzerland,120,100,80,50,0.05,Yes,All,,Yes,Recommended,Recommended,No,18,Yes
CY,Cyprus,100,N/A,80,50,0.05,,All,12,"Yes, 2x",Recommended,Recommended,No,18,Yes
CZ,Czech Republic,130 (urban 80),130 (urban 80),90,50,0 (tolerated 0.02),Yes,All,12,Yes,Yes,Recommended,Yes,18 (17 for B1 cars),Yes
D,Germany [2],no (130 recommended),no (130 recommended),100 (no / 130 recommended with two or more lanes per direction),50,0.05,No[3],All,N/A,Yes,Yes,Recommended,No,18 (17 with supervision of >30 years old driver),Yes
DK,Denmark,130,N/A,80,50,0.05,,All,N/A,Yes,Recommended,Recommended,No,18,Yes
E,Spain,120,120,90,50,0.05,Yes,All,N/A,"Yes, 2x",Recommended,Recommended,No,18,Yes
EST,Estonia,N/A,110,90,50,0,,All,N/A,Yes,Yes,Yes,No,18,Yes
F,France,130 (rain/wet 110),110 (rain/wet 100),90 (rain/wet 80),50,0.05,Yes,All,10,Yes,Recommended,Recommended,No,18 (16 with supervision of parents),Yes
FIN,Finland,120,N/A,80,50,0.05,,All,3,Yes,Recommended,Recommended,No,18,Yes
FL,Liechtenstein,N/A,N/A,80,50,0.08,,All,N/A,Yes,Yes,Recommended,No,18,Yes
GB,United Kingdom,112,112,97,48,0.08,,All,N/A,Recommended,Recommended,Recommended,No,17,Yes
GR,Greece,130,N/A,90,50,0.05,,All,12,Yes,Yes,Yes,No,18,Yes
H,Hungary,130,110,90,50,0,Yes,All,12,Yes,Yes,Recommended,No,17,Yes
HR,Croatia,130,110,90,50,0,Yes,All,12,Yes,Yes,Recommended,No,18,Yes
I,Italy,130,110,90,50,0.05,Yes,All,12,Yes,Recommended,Recommended,No,18,Yes
IRL,Ireland,120,N/A,100,50,0.08,Yes[4],All,N/A,Recommended,Recommended,Recommended,No,17,Yes
IS,Iceland,N/A,N/A,90,50,0.05,,All,14,Yes,Recommended,Recommended,No,17,Yes
L,Luxembourg,130,N/A,90,50,0.05,,All,11,Yes,Recommended,Recommended,No,18,Yes
LT,Lithuania,130,110,90,50,0.04,,All,12,Yes,Yes,Yes,No,16,No
LV,Latvia,110,N/A,90,50,0.05,,All,,Yes,Yes,Yes,No,18,Yes
M,Malta,N/A,N/A,80,50,0.08,,Front,11,Recommended,Recommended,Recommended,No,18,Yes
MC,Monaco,N/A,N/A,,50,0.05,,N/A,10,Recommended,Recommended,Recommended,No,18,Yes
MD,Moldova,N/A,N/A,90,60,0,,All,12,Yes,Yes,Yes,No,18,Yes
MK,Macedonia,130,N/A,80,60,0.05,Yes,All,12,Yes,Yes,Recommended,Yes,18,Yes
MNE,Montenegro,120,N/A,80,50,0.05,Yes,All,12,Yes,Recommended,Recommended,No,,Yes
N,Norway,100,N/A,80,50,0.02,,All,N/A,Yes,Recommended,Recommended,No,18,Yes
NL,Netherlands,130,100,80,50,0.05,Yes[5],All,12,Yes,Recommended,Recommended,No,18(16 with supervision of someone with 5+ years drive-experience,Yes
PT,Portugal,120,100,90,50,0.05,Yes,All,12,Yes,Recommended,Recommended,No,18,Yes
PL,Poland,140,100 (120 on expressways),90 (100 on expressways),"50 (hours 5 till 23), 60 (hours 23 till 5)",0.02,Yes,All,N/A,Yes,Recommended,Yes,No,18 (16 for B1 cars),No
RO,Romania,130,N/A,90,50,0,Yes,All,12,Yes,Yes,Yes,Yes,18 (16 for B1 cars),No
RSM,San Marino,N/A,N/A,70,50,0.08,,All,12,Recommended,Recommended,Recommended,No,18,Yes
RUS,Russia [6],110,N/A,90,60,0,,All,12,Yes,Yes,Yes,No,18,Yes
S,Sweden,110,N/A,70,50,0.02,,All,N/A,Yes,Recommended,Recommended,No,18,Yes
SK,Slovakia,130,130,90,50,0,Yes,All,12,Yes,Yes,Recommended,Yes,18,Yes
SLO,Slovenia,130,100,90,50,0.05,,All,12,Yes,Yes,Recommended,No,18,Yes
SRB,Serbia,120,100,80,50,0.03,Yes,All,12,Yes,Yes,Yes,No,18,Yes
TR,Turkey,120,N/A,90,50,0.05,,All,10,Yes,Yes,Recommended,No,18,Yes
UA,Ukraine,130,110,90,60,0,Yes,All,12,Yes,Yes,Yes,No,18,Yes"""
  var code = """{| class="wikitable sortable" style="text-align: center; width: auto; font-size: smaller;"
|-
! [[ISO_3166-1|Country code]] !! Country !! Speed limit at [[motorway]] (km/h) !! Speed limit at [[dual carriageway]] (km/h) !! Speed limit at [[single carriageway]] (km/h) !! Speed limit at urban area !! Permitted alcohol level (%) !! [[Toll_road|Toll roads]] !! Seatbelt required !! Minimum child age (front seat) !! Triangle required !! First aid required !! Fire extinguisher required !! Spare bulb required !! Minimum driver's age !! Tow rope required
|-
! A
|align="left"| {{AUT}} || 130 || 100 || 100 || 50 || 0.05 || {{Yes}} || {{yes|All}} || {{n/a}} || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{No}} || 17 || {{Yes}}
|-
! AL
|align="left"| {{ALB}} || 110 || {{n/a}} || 90 || 40 || 0 || || {{n/a}} || {{n/a}} || {{partial|Recommended}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || {{n/a}} || {{Yes}}
|-
! AND
|align="left"| {{AND}} || {{n/a}} || {{n/a}} || 90 || 50 || 0.05 || || {{partial|Front}} || 10 || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! B
|align="left"| {{BEL}} || 120 || 120 || 90 || 50 || 0.05 || {{No}}<ref>{{cite web | url=http://www.euroadlegal.co.uk/country/belgium.html | title=Laws and Tips for Driving in Belgium - EUroadlegal.co.uk | publisher=EUroadlegal.co.uk | accessdate=14 August 2013}}</ref> || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! BG
|align="left"| {{BGR}} || 140 || {{n/a}} || 90 || 50 || 0.05 || {{Yes}} || {{yes|All}} || 10 || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! BIH
|align="left"| {{BIH}} || 130 || 100 || 80 || 50 || 0.03 || || {{partial|Front}} || 12 || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{Yes}} || 18 || {{Yes}}
|-
! BY
|align="left"| {{BLR}} || 110 || {{n/a}} || 90 || 60 || <0.03 || {{Yes}} || {{partial|Front}} || 12 || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! CH
|align="left"| {{CHE}} || 120 || 100 || 80 || 50 || 0.05 || {{Yes}} || {{yes|All}} || || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! CY
|align="left"| {{CYP}} || 100 || {{n/a}} || 80 || 50 || 0.05 || || {{yes|All}} || 12 || {{yes|Yes, 2x}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! CZ
|align="left"| {{CZE}} || 130 (urban 80) || 130 (urban 80) || 90 || 50 || 0 (tolerated 0.02) || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{Yes}} || 18 (17 for B1 cars) || {{Yes}}
|-
! D
|align="left"| {{DEU}} <ref>{{cite web|url=http://www.gesetze-im-internet.de/stvo_2013/index.html|title=Germany Traffic Code|publisher=www.gesetze-im-internet.de|language=German|date= |accessdate=2013-07-15}}</ref> || no (130 recommended) || no (130 recommended)|| 100 (no / 130 recommended with two or more lanes per direction) || 50 || 0.05 || {{No}}<ref name="Germany EUroadlegal">{{cite web | url=http://www.euroadlegal.co.uk/country/germany.html | title=Laws and Tips for Driving in Germany - EUroadlegal.co.uk | publisher=EUroadlegal.co.uk | accessdate=14 August 2013}}</ref> || {{yes|All}} || {{n/a}} || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{No}} || 18 (17 with supervision of >30 years old driver) || {{Yes}}
|-
! DK
|align="left"| {{DNK}} || 130 || {{n/a}} || 80 || 50 || 0.05 || || {{yes|All}} || {{n/a}} || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! E
|align="left"| {{ESP}} || 120 || 120 || 90 || 50 || 0.05 ||{{Yes}} || {{yes|All}} || {{n/a}} || {{yes|Yes, 2x}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! EST
|align="left"| {{EST}} || 110 || {{n/a}} || 90 || 50 || 0 || || {{yes|All}} || {{n/a}} || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! F
|align="left"| {{FRA}} || 130 (rain/wet 110) || 110 (rain/wet 100) || 90 (rain/wet 80) || 50 || 0.05 ||{{Yes}} || {{yes|All}} || 10 || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 (16 with supervision of parents) || {{Yes}}
|-
! FIN
|align="left"| {{FIN}} || 120 || {{n/a}} || 80 || 50 || 0.05 || || {{yes|All}} || 3 || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! FL
|align="left"| {{LIE}} || {{n/a}} || {{n/a}} || 80 || 50 || 0.08 || || {{yes|All}} || {{n/a}} || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! GB
|align="left"| {{UK}} || 112 || 112 || 97 || 48 || 0.08 || || {{yes|All}} || {{n/a}} || {{partial|Recommended}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 17 || {{Yes}}
|-
! GR
|align="left"| {{GRC}} || 130 || {{n/a}} || 90 || 50 || 0.05 || || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! H
|align="left"| {{HUN}} || 130 || 110 || 90 || 50 || 0 || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{No}} || 17 || {{Yes}}
|-
! HR
|align="left"| {{HRV}} || 130 || 110 || 90 || 50 || 0 || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! I
|align="left"| {{ITA}} || 130 || 110 || 90 || 50 || 0.05 ||{{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! IRL
|align="left"| {{IRL}} || 120 || {{n/a}} || 100 || 50 || 0.08 || {{Yes}}<ref>{{cite web | url=http://www.euroadlegal.co.uk/country/ireland.html | title=Laws and Tips for Driving in Ireland - EUroadlegal.co.uk | publisher=EUroadlegal.co.uk | accessdate=14 August 2013}}</ref> || {{yes|All}} || {{n/a}} || {{partial|Recommended}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 17 || {{Yes}}
|-
! IS
|align="left"| {{ISL}} || {{n/a}} || {{n/a}} || 90 || 50 || 0.05 || || {{yes|All}} || 14 || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 17 || {{Yes}}
|-
! L
|align="left"| {{LUX}} || 130 || {{n/a}} || 90 || 50 || 0.05 || || {{yes|All}} || 11 || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! LT
|align="left"| {{LTU}} || 130 || 110 || 90 || 50 || 0.04 || || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 16 || {{No}}
|-
! LV
|align="left"| {{LVA}} || 110 || {{n/a}} || 90 || 50 || 0.05 || || {{yes|All}} || || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! M
|align="left"| {{MLT}} || {{n/a}} || {{n/a}} || 80 || 50 || 0.08 || || {{partial|Front}} || 11 || {{partial|Recommended}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! MC
|align="left"| {{MCO}} || {{n/a}} || {{n/a}} || || 50 || 0.05 || || {{n/a}} || 10 || {{partial|Recommended}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! MD
|align="left"| {{MDA}} || {{n/a}} || {{n/a}} || 90 || 60 || 0 || || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! MK
|align="left"| {{MKD}} || 130 || {{n/a}} || 80 || 60 || 0.05 || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{Yes}} || 18 || {{Yes}}
|-
! MNE
|align="left"| {{MNE}} || 120 || {{n/a}} || 80 || 50 || 0.05 || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || || {{Yes}}
|-
! N
|align="left"| {{NOR}} || 100 || {{n/a}} || 80 || 50 || 0.02 || || {{yes|All}} || {{n/a}} || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! NL
|align="left"| {{NLD}} || 130 || 100 || 80 || 50 || 0.05 || {{Yes}}<ref>{{cite web | url=http://www.euroadlegal.co.uk/country/holland.html | title=Laws and Tips for Driving in Holland - EUroadlegal.co.uk | publisher=EUroadlegal.co.uk | accessdate=14 August 2013}}</ref> || {{yes|All}} || 12 || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! PT
|align="left"| {{PRT}} || 120 || 100 || 90 || 50 || 0.05 || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! PL
|align="left"| {{POL}} || 140 || 100 (120 on expressways) || 90 (100 on expressways) || 50 (hours 5 till 23), 60 (hours 23 till 5) || 0.02 || {{Yes}} || {{yes|All}} || {{n/a}} || {{Yes}} || {{partial|Recommended}} || {{Yes}} || {{No}} || 18 || {{No}}
|-
! RO
|align="left"| {{ROU}} || 130 || {{n/a}} || 90 || 50 || 0 || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{Yes}} || {{Yes}} || 18 (16 for B1 cars) || {{No}}
|-
! RSM
|align="left"| {{SMR}} || {{n/a}} || {{n/a}} || 70 || 50 || 0.08 || || {{yes|All}} || 12 || {{partial|Recommended}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! RUS
|align="left"| {{RUS}} <ref>{{cite web|url=http://www.gibdd.ru/bitrix/redirect.php?event1=news_out&event2=%2Fupload%2Fiblock%2F523%2F523b3285119d6482a6135af550cb70ff.doc&event3=%D0%9F.%D0%9F.+1090.doc&goto=%2Fupload%2Fiblock%2F523%2F523b3285119d6482a6135af550cb70ff.doc|title=Russian Federation Traffic Code|language=Russian|format=DOC|publisher=www.gibdd.ru|date= |accessdate=2013-07-15}}</ref> || 110 || {{n/a}} || 90 || 60 || 0 || || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! S
|align="left"| {{SWE}} || 110 || {{n/a}} || 70 || 50 || 0.02 || || {{yes|All}} || {{n/a}} || {{Yes}} || {{partial|Recommended}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! SK
|align="left"| {{SVK}} || 130 || 130 || 90 || 50 || 0 || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{Yes}} || 18 || {{Yes}}
|-
! SLO
|align="left"| {{SVN}} || 130 || 100 || 90 || 50 || 0.05 || || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! SRB
|align="left"| {{SRB}} || 120 || 100 || 80 || 50 || 0.05 || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! TR
|align="left"| {{TUR}} || 120 || {{n/a}} || 90 || 50 || 0.05 || || {{yes|All}} || 10 || {{Yes}} || {{Yes}} || {{partial|Recommended}} || {{No}} || 18 || {{Yes}}
|-
! UA
|align="left"| {{UKR}} || 130 || 110 || 90 || 60 || 0 || {{Yes}} || {{yes|All}} || 12 || {{Yes}} || {{Yes}} || {{Yes}} || {{No}} || 18 || {{Yes}}
|-
! [[ISO_3166-1|Country code]] !! Country !! Speed limit at [[motorway]] (km/h) !! Speed limit at [[dual carriageway]] (km/h) !! Speed limit at [[single carriageway]] (km/h) !! Speed limit at urban area !! Permitted alcohol level (%) !! [[Toll_road|Toll roads]] !! Seatbelt required !! Minimum child age (front seat) !! Triangle required !! First aid required !! Fire extinguisher required !! Spare bulb required !! Minimum driver's age !! Tow rope required
|}"""

  var pcm1 : PCM = _
  var pcm2 : PCM = _

  override def beforeAll(): Unit = {
    pcm1 = pcmFactory.createPCM()
    pcm2 = pcmFactory.createPCM()
    pcm1 = pcmExporter.export(miner.parse(miner.preprocess(code), title)).head
  }

  "A PCM" should "be identical to the wikitext it came from" in {
    pcm2 = csvLoader.load(csv)
    var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)

    diff.hasDifferences shouldBe false
  }
  it should "be the same that the creation of an other one from it's wikitext representation" in {
    val preprocessedCode2 = miner.preprocess(wikiTextExporter.toWikiText(pcm1))
    pcm2 = pcmExporter.export(miner.parse(preprocessedCode2, title)).head
    var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)

    diff.hasDifferences shouldBe false
  }
}
