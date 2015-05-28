package org.opencompare.experimental.io.wikipedia

import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by gbecan on 5/20/15.
 */
class Sweble2Test extends FlatSpec with Matchers {

  val title = "Comparison (grammar)"
  val code = """{{Update|inaccurate=yes|section|date=April 2014}}
{| class="wikitable sortable" style="text-align: center; font-size: 85%; width: auto; table-layout: fixed;"
|-
! style="width: 12em" |
! Creator
! First public release date
! Latest stable release
! Stable release date
! Predecessor
! Software license<ref name="license">Licenses here are a summary, and are not taken to be complete statements of the licenses. Some packages may use libraries under different licenses.</ref>
! Open source
! Encoding
! Multilingual
! Programming language
! [[Data management|Data backend]]
|-
| {{rh}} | [[BlueSpice for MediaWiki]]
| Hallo Welt! - Medienwerkstatt GmbH
| {{dts|format=dmy|nowrap=off|2011|March|31}}
| 2.23.0
| {{dts|format=dmy|nowrap=off|2014|December|09}}<ref name=changelog>{{cite web|url=http://help.blue-spice.org/index.php/BlueSpice_2.23/Release_Notes|title=Changelog|work=help.blue-spice.org|accessdate=2014-12-09}}</ref>
| [[MediaWiki]]
| [[GNU General Public License|GPL]] v2
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[PHP]]
| [[MySQL]]
|-
| {{rh}} | [[BrainKeeper]]
| BrainKeeper, Inc.
| {{dts|format=dmy|nowrap=off|2005|September|1}}
|
| {{dts|format=dmy|nowrap=off|2010|March|1}}
|
| [[Proprietary software|Proprietary]]
| {{No}}
|
| {{Yes}}
| [[Java (programming language)|Java]]/[[Java EE]]
| [[MySQL]]
|-
| {{rh}} | [[Central Desktop]]
| Central Desktop Inc.
| {{dts|format=dmy|nowrap=off|2005|October|1}}
| 2.0
| {{dts|format=dmy|nowrap=off|2010|February|22}}
|
| [[Proprietary software|Proprietary]]
| {{No}}
|
|
| [[PHP]]
| [[PostgreSQL]]
|-
| {{rh}} | [[Atlassian Confluence|Confluence]]
| [[Atlassian Software Systems]]
| {{dts|format=dmy|nowrap=off|2004|March|25}}
| 5.7
| {{dts|format=dmy|nowrap=off|2015|February|10}}<ref>http://blogs.atlassian.com/2015/02/introducing-new-feedback-loop-confluence-5-7/</ref>
|
| [[Proprietary software|Proprietary]]
| {{No}}<ref name=CNFSRC>Atlassian Confluence Licensing FAQs({{Cite web|url=http://www.atlassian.com/licensing/purchase-licensing#source-1|title=Do I get access to the source code?}})</ref>
| [[UTF-8]]
| {{Yes}}
| [[Java (programming language)|Java]], [[Java EE]]
| [[IBM DB2|DB2]], [[MS SQL Server]], [[MySQL]], [[Oracle database|Oracle]], or [[PostgreSQL]]
|-
| {{rh}} | [[ConnectedText]]
| Eduardo Mauro
| {{dts|format=dmy|nowrap=off|2005}}
| 6.0.12
| {{dts|format=dmy|nowrap=off|2015|February|21}}
|
| [[Proprietary software|Proprietary]]
| {{No}}
|
| {{Yes}}
| [[C++]]
| [[SQL]]
|-
| {{rh}} | [[DokuWiki]]
| Andreas Gohr
| {{dts|format=dmy|nowrap=off|2004|July|}}
| 2014-09-29<ref>https://www.dokuwiki.org/changes</ref>
| {{dts|format=dmy|nowrap=off|2014|September|29}}
|
| [[GNU General Public License#Version 2|GPL v2]]
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[PHP]]
| [[File system]]
|-
| {{rh}} | [[EditMe]]
| Matt Wiseley
| {{dts|format=dmy|nowrap=off|2003|August|}}
|
|
|
| [[Proprietary software|Proprietary]]
| {{No}}
| [[ISO8859-1]]
| {{No}}
| [[Java (programming language)|Java]]
| [[MySQL]]
|-
| {{rh}} | [[eXo Platform]]
| [[eXo Platform]]
| 2002
| 4.0
| {{dts|format=dmy|nowrap=off|April 2014}}
|
| [[GNU Library General Public License|LGPL]]
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[Java (programming language)|Java]]
| [[PostgreSQL]], [[MySQL]], Oracle, Apache Derby, HSQLDB
|-
| {{rh}} | [[FlexWiki]]
| David Ornstein
| September 2004
| 2.1.0.274
| {{dts|format=dmy|nowrap=off|2008|October|20}}
|
| [[Common Public License|CPL]]
| {{Yes}}
|
|
| [[ASP.NET]], [[C Sharp (programming language)|C#]]
| [[File system]], [[Microsoft SQL Server]]
|-
| {{rh}} | [[Foswiki]]
| Foswiki community
| {{dts|format=dmy|nowrap=off|2008|December|}}
| 1.1.9
| {{dts|format=dmy|nowrap=off|2013|March|1}}
| TWiki
| [[GNU General Public License|GPL]]
| {{Yes}}
| [[ISO8859-1]], [[UTF-8]]
| {{Yes}}
| [[Perl]]
| [[Flat-file]], [[Revision Control System|RCS]], pluggable storage backend<ref name="n2.nabble.com">[http://n2.nabble.com/Foswiki-on-Wikipedia-td3698728i20.html#a3728148 Foswiki mailing list - Foswiki on Wikipedia]</ref>
|-
| {{rh}} | [[Gitit (software)|Gitit]]
| John MacFarlane and community
| | {{dts|format=dmy|nowrap=off|2008|November|7}}
| 0.10.3.1<ref>{{cite web|title=gitit/CHANGES at master • jgm/gitit|url=https://github.com/jgm/gitit/blob/master/CHANGES|work=github.com|accessdate=2 August 2013}}</ref>
| | {{dts|format=dmy|nowrap=off|2013|Mar|19}}
|
| [[GNU General Public License|GPL]]
| {{yes}}
| [[UTF-8]]
| {{yes}}
| [[Haskell (programming language)|Haskell]]
| [[Git (software)|Git]], now also [[Darcs]], [[Mercurial]]
|-
| {{rh}} | [[GrokOla Software|Grokola]]
| Keyhole Labs
| | {{dts|format=dmy|nowrap=off|2014|September}}
| 1.3 MockOla<ref>https://keyholesoftware.com/2015/03/18/keyhole-releases-mockola/</ref>
| {{dts|format=dmy|nowrap=off|2015|March|}}
|
| [[Proprietary software|Proprietary]]
| {{no}}
| [[UTF-8]]
| {{yes}}
| [[JavaScript (programming language)|JavaScript]]
| [[Node.js]], [[Java (programming language)|Java]]
|-
| {{rh}} | [[IBM Connections]]
| [[IBM]]
| {{dts|format=dmy|nowrap=off|2007|June|27}}
| 4.5 CR1
| {{dts|format=dmy|nowrap=off|2013|June|26}}
|
| [[Proprietary software|Proprietary]]
| {{No}}
| [[UTF-8]]
| {{yes}}
| [[Java (programming language)|Java]]/[[Java EE]]
| [[IBM DB2|DB2]], [[MS SQL Server]] or [[Oracle database|Oracle]]
|-
| {{rh}} | [[Ikiwiki]]
| Joey Hess et al.<ref>{{cite web |url=http://ikiwiki.info/ |title=ikiwiki |author= |date= |work= |publisher=ikiwiki.info |accessdate=2012-02-01 |quote=Ikiwiki is developed by [http://ikiwiki.info/users/joey/ Joey] and many contributors.}}</ref>
| {{dts|format=dmy|nowrap=off|2006|April|29}}<ref>{{cite web |url=http://ikiwiki.info/roadmap/ |title=roadmap |author= |date= |work= |publisher=ikiwiki.info |accessdate=2012-02-01 |quote=Released 29 April 2006.}}</ref>
| 3.20140227<ref>{{citation |url=http://ikiwiki.info/news/version_3.20141016/ |title= ikiwiki/ news/ version 3.20141016 |first=Joey |last=Hess |work=[[ikiwiki]] |date=2014-11-24 |accessdate=2014-12-20}}</ref>
| {{dts|format=dmy|nowrap=off|2014|November|24}}
|
| [[GNU General Public License]] v2 +<ref name="ikiwikifree">{{cite web |url=http://ikiwiki.info/freesoftware/ |title=Free Software |author= |date= |work= |publisher=ikiwiki.info |accessdate=2012-02-01}}</ref>
| {{Yes}}<ref name="ikiwikifree"/>
|
| {{Yes}}
| [[Perl]]<ref>{{cite web |url=http://ikiwiki.info/install/ |title=install |author= |date= |work= |publisher=ikiwiki.info |accessdate=2012-02-01}}</ref>
| standard [[version control system]]<ref name="features">{{Cite web|url=http://ikiwiki.info/features/#index1h2 |title=ikiwiki feature "Use a Real RCS" |publisher=Ikiwiki.info |date= |accessdate=2010-01-29}}</ref> such as [[Git (software)|Git]] or [[Subversion (software)|Subversion]] or 6+ others<ref name="rcs_support">{{Cite web|url=http://ikiwiki.info/rcs |title=ikiwiki Revision Control Systems |publisher=Ikiwiki.info |date= |accessdate=2010-01-29}}</ref>
|-
| {{rh}} | [[Jive (software)|Jive]]
| [[Jive Software]]
| {{dts|format=dmy|nowrap=off|2007|January|7}}
| 5.0.3
| {{dts|format=dmy|nowrap=off|2012|April|25}}
|
| [[Proprietary software|Proprietary]]
| {{No}}
| [[ISO8859-1]], [[UTF-8]], ...
| {{Yes}}
| [[Java EE]]/[[Java (programming language)|Java]]
| [[SQL]]/[[LDAP]]<ref>including: [[MySQL]], [[Oracle database|Oracle]], [[PostgreSQL]], [[IBM DB2]] and [[Microsoft SQL Server]]</ref>
|-
| {{rh}} | [[JotSpot]]
| JotSpot (now Google Sites)
| {{dts|format=dmy|nowrap=off|2004|October|}}
| 2.0
| {{dts|format=dmy|nowrap=off|2006|July|24}}
|
| [[Proprietary software|Proprietary]]
| {{No}}
|
|
| [[Java (programming language)|Java]]
| [[File system]], [[XML]]
|-
| {{rh}} | [[MediaWiki]]
| [[Magnus Manske]]
| {{dts|format=dmy|nowrap=off|2002|January|25}}
| 1.23.1
| {{dts|format=dmy|nowrap=off|2014|June|25}}<ref>http://lists.wikimedia.org/pipermail/mediawiki-announce/2014-June/000155.html</ref>
|
| [[GNU General Public License|GPL]] v2
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[PHP]]
| [[MySQL]], [[PostgreSQL]], [[SQLite]], and others
|-
| {{rh}} | [[MetaTeam]]
| [[Altova]]
| {{dts|format=dmy|nowrap=off|2012|May|6}}
| 1.0
| {{dts|format=dmy|nowrap=off|2012|May|6}}
|
| [[Proprietary software|Proprietary]]
| {{no}}
| [[UTF-8]]
| {{Yes}}
|
| Online service
|-
| {{rh}} | [[Midgard (software)|Midgard Wiki]]
| Henri Bergius
| {{dts|format=dmy|nowrap=off|2004|September|29}}
| 10.05.6<ref>[http://www.midgard-project.org/updates/midgard_10-05-6_ratatoskr_lts_released/ Midgard-Project.org]</ref>
| {{dts|format=dmy|nowrap=off|2012|March|6}}
|
| [[GNU Lesser General Public License|LGPL]]
| {{Yes}}
|
|
| [[PHP]]
| [[MySQL]] and [[Revision Control System|RCS]]
|-
| {{rh}} | [[MindTouch]]
| MindTouch Inc.
| {{dts|format=dmy|nowrap=off|2006|July|25}}
| 10.1.4
| {{dts|format=dmy|nowrap=off|2013|January|22}}
| [[MediaWiki]]
| [[Proprietary software|Proprietary]]
| {{No}}
| [[UTF-8]]
| {{Yes}}
| [[PHP]], [[C Sharp (programming language)|C#]] on Windows or [[Mono (software)|Mono]]
| [[MySQL]]
|-
| {{rh}} | [[MoinMoin]]
| Jürgen Hermann; Thomas Waldmann; ...
| {{dts|format=dmy|nowrap=off|2000|July|28}}
| 1.9.7
| {{dts|format=dmy|nowrap=off|2013|March|17}}
| PikiPiki
| [[GNU General Public License|GPL]]
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[Python (programming language)|Python]]
| [[Flat-file]]
|-
| {{rh}} | [[MojoMojo]]
| Marcus Ramberg & community
| {{dts|format=dmy|nowrap=off|2007|August|29}}
| 1.09
| {{dts|format=dmy|nowrap=off|2013|January|25}}
|
|
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[Perl]]
| [[PostgreSQL]], [[SQLite]], [[MySQL]], others
|-
| {{rh}} | [[PBworks]]
| David Weekly
| {{dts|format=dmy|nowrap=off|2005|May|30}}
|
|
| [[TipiWiki]]
| [[Proprietary software|Proprietary]]
| {{No}}
| [[UTF-8]]
|
| [[PHP]]
| [[MogileFS]], [[Squid (software)|Squid]], [[MySQL]], [[Pound (networking)|Pound]], [[lighttpd]]
|-
| {{rh}} | [[PhpWiki]]
| Steve Wainstead, ...
| {{dts|format=dmy|nowrap=off|1999|December|}}
| 1.5.3<ref>http://sourceforge.net/projects/phpwiki/</ref>
| {{dts|format=dmy|nowrap=off|2015|March|4}}
| [[WikiWikiWeb]]
| [[GNU General Public License|GPL]]
| {{Yes}}
| [[ISO8859-1]], [[UTF-8]]
| {{Yes}}
| [[PHP]]
| [[Berkeley DB]], [[Flat-file]], [[MySQL]], [[PostgreSQL]], [[Microsoft SQL Server]], Oracle 8, Firebird
|-
| {{rh}} | [[PmWiki]]
| Patrick Michaud
| {{dts|format=dmy|nowrap=off|2002|February|6}}
| 2.2.62
| {{dts|format=dmy|nowrap=off|2014|February|28}}
|
| [[GNU General Public License|GPL]]
| {{Yes}}
| [[ISO8859-1]], [[UTF-8]]
| {{Yes}}
| [[PHP]]
| [[Flat-file]]. MySQL, SQLite (plug-ins)
|-
| {{rh}} | [[Qontext]]
| Qontext, Inc
| {{dts|format=dmy|nowrap=off|2010|November|6}}
| 0.144.1
| {{dts|format=dmy|nowrap=off|2010|December|12}}
|
| [[Proprietary software|Proprietary]]
| {{No}}
| [[UTF-8]]
| {{Yes}}
| [[Java (programming language)|Java]]
| [[Amazon Elastic Compute Cloud|Amazon Cloud]]
|-
| {{rh}} | [[Microsoft SharePoint|SharePoint]]
| [[Microsoft]]
| {{dts|format=dmy|nowrap=off|2001|March}}
| 2010
| {{dts|format=dmy|nowrap=off|2010|April|22}}
| 3.0
| [[Proprietary software|Proprietary]]
| {{No}}
| [[UTF-8]]
| {{Yes}}
| [[ASP.NET]], [[C Sharp (programming language)|C#]]
| [[Microsoft SQL Server]] or [[Windows Internal Database]]
|-
| {{rh}} | [[SlimWiki]]
| [[SlimWiki]]
| {{dts|format=dmy|nowrap=off|2014|July}}
| 2014
| 2014
|
| [[Proprietary software|Proprietary]]
| {{No}}
| [[UTF-8]]
| {{Yes}}
| [[Ruby]]
| [[PostgreSQL]]
|-
| {{rh}} | [[Socialtext]]
| Socialtext
| {{dts|format=dmy|nowrap=off|2003}}
| 5.0.0
| {{dts|format=dmy|nowrap=off|ink=off|2011|September|28}}
| N/A
| Dual ([[CPAL]]) and [[Proprietary software|Proprietary]])
|
| [[UTF-8]]
|
| [[Perl]]
| [[PostgreSQL]]
|-
| {{rh}} | [[Swiki]]
| [[Mark Guzdial]]; Jochen Rick
| {{dts|format=dmy|nowrap=off|1999|October|}}
| 1.5
| {{dts|format=dmy|nowrap=off|2005|December|6}}
|
| [[GNU General Public License|GPL]]
| {{Yes}}
|
|
| [[Squeak]]
| [[File system]]
|-
| {{rh}} | [[ThoughtFarmer]]
| ThoughtFarmer
| {{dts|format=dmy|nowrap=off|2006|May|}}
| 5.5
| {{dts|format=dmy|nowrap=off|2012|June|4}}
|
| [[Proprietary software|Proprietary]]
| {{No}}
| [[UTF-8]]
| {{Yes}}
| [[ASP.NET]], [[C Sharp (programming language)|C#]]
| [[Microsoft SQL Server]]
|-
| {{rh}} | [[TiddlyWiki]]
| Jeremy Ruston
| {{dts|format=dmy|nowrap=off|2004|September|}}
| 5.1.7
| {{dts|format=dmy|nowrap=off|2011|October|6}}
|
| [[BSD licenses|BSD]]
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[JavaScript]]
| Single file, MySQL (mod)
|-
| {{rh}} | [[Tiki Wiki CMS Groupware]]
| Luis Argerich (200+ devs nowadays)
| {{dts|format=dmy|nowrap=off|2002|October|9}}
| 13.1
| {{dts|format=dmy|nowrap=off|2014|Nov|14}}<ref>http://info.tiki.org/article230</ref>
|
| [[GNU Lesser General Public License|LGPL]]
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[PHP]]
| [[MySQL]]
|-
| {{rh}} | [[Trac]]
| Edgewall Software
| {{dts|format=dmy|nowrap=off|2006|October|1}}
| 1.0.4
| {{dts|format=dmy|nowrap=off|2015|February|8}}
|
| [[BSD license|BSD]]
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[Python (programming language)|Python]]
| [[MySQL]], [[PostgreSQL]], [[SQLite]]
|-
| {{rh}} | [[Traction TeamPage]]
| Traction Software
| {{dts|format=dmy|nowrap=off|1999|December|1}}
| 6.0
| {{dts|format=dmy|nowrap=off|2014|June|2}}
|
| [[Proprietary software|Proprietary]]
| {{No}}
| [[UTF-8]], ...
| Supports multi-lingual content<br>(and supports [[i18n]])
| [[Java SE]]/[[Java (programming language)|Java]]
| Flat File and File System<br>Oracle 10G RDB option <br>WebDAV for Attachments
|-
| {{rh}} | [[TWiki]]
| Peter Thoeny
| {{dts|format=dmy|nowrap=off|1998|October|}}
| 6.0.0
| {{dts|format=dmy|nowrap=off|2013|October|14}}
| JosWiki
| [[GNU General Public License|GPL]]
| {{Yes}}
| [[ISO8859-1]], [[UTF-8]]
| {{Yes}}
| [[Perl]]
| [[Flat-file]], [[Revision Control System|RCS]], pluggable storage backend<ref name="n2.nabble.com"/>
|-
| {{rh}} | [[UseModWiki]]
| Clifford Adams
| {{dts|format=dmy|nowrap=off|2000|January|22}}
| 1.0.5
| {{dts|format=dmy|nowrap=off|2009|August|28}}
| AtisWiki
| [[GNU General Public License|GPL]]
| {{Yes}}
|
|
| [[Perl]]
| [[Flat-file]]
|-
| {{rh}} | [[WackoWiki]]
| Roman Ivanov
| {{dts|format=dmy|nowrap=off|2003|March|}}
| 5.4.0
| {{dts|format=dmy|nowrap=off|2014|April|17}}
| [[WakkaWiki]]
| [[BSD license|BSD]]
| {{Yes}}
|
| {{Yes}}
| [[PHP]]
| [[MySQL]]
|-
| {{rh}} | [[Wagn (software)|Wagn]]
| Grass Commons
| {{dts|format=dmy|nowrap=off|2006|April|}}
| 1.12.0
| {{dts|format=dmy|nowrap=off|2013|September|11}}
| Instiki
| [[GNU General Public License|GPL]]
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[Ruby (programming language)|Ruby]]
| [[MySQL]], [[PostgreSQL]]
|-
! {{rh}} | [[Wiki Server]]
| [[Apple Inc.]]
|?
|
|
|
| [[Proprietary software|Proprietary]]
| {{No}}
|
|
|
|
|-
! {{rh}} | [[Wikispaces]]
| Tangient LLC
| {{dts|format=dmy|nowrap=off|2005|March|18}}
|
|
|
| [[Proprietary software|Proprietary]]
| {{No}}
| [[UTF-8]]
| {{Yes}}
| [[PHP]]
| [[MySQL]], [[MogileFS]]
|-
! {{rh}} | [[Wikiwig]]
| Steve Goldman (Formerly Starcrouz)
| {{dts|format=dmy|nowrap=off|2004|July|20}}
| 5.01
| {{dts|format=dmy|nowrap=off|2008|May|29}}
| Wikiwig 4.x
| [[GNU General Public License|GPL]]
| {{Yes}}
|
| {{Yes}}
| [[PHP]]
| [[MySQL]]
|-
! {{rh}} | [[WikiWikiWeb]]
| [[Ward Cunningham]]
| {{dts|format=dmy|nowrap=off|1995}}
|
|
| [http://c2.com/cgi/wiki?WikiWikiHyperCard WikiWiki in HyperCard]<ref>{{cite web|last1=Cunningham|first1=Ward|title=WikiHistory|url=http://c2.com/cgi/wiki?WikiHistory|accessdate=25 August 2014|location=http://c2.com/cgi/wiki?WikiHistory|quote="An early page, WikiWikiHyperCard, traces wiki ideas back to a HyperCard stack I wrote in the late 80's. This same stack, by the way, spawned CrcCards. I've reconstructed the WikiDesignPrinciples I applied at the time."}}</ref>
|
|
|
|
| Perl
| [[File System]]
|-
! {{rh}} | [[WikkaWiki]]
| Wikka Development Team
| {{dts|format=dmy|nowrap=off|2004|May|16}}
| 1.3.6
| {{dts|format=dmy|nowrap=off|2014|Dec|24}}
| [[WakkaWiki]]
| [[GNU General Public License|GPL]] (code) and [[CC-BY-SA]] (docs)
| {{Yes}}
| [[ISO8859-1]]
| {{Yes}}
| [[PHP]]
| [[MySQL]]
|-
| {{rh}} | [[XWiki]]
| Ludovic Dubost
| {{dts|format=dmy|nowrap=off|2004|February|}}
| 7.0.1
| {{dts|format=dmy|nowrap=off|2015|April|30}}
| TWiki
| [[GNU Library General Public License|LGPL]]
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[Java (programming language)|Java]]
| [[PostgreSQL]], [[MySQL]], Oracle, Apache Derby, HSQLDB
|-
| {{rh}} | [[ZWiki]]
| Simon Michael
| {{dts|format=dmy|nowrap=off|1999|November|5}}
| 0.61.0
| {{dts|format=dmy|nowrap=off|2008|October|28}}
|
| [[GNU General Public License|GPL]]
| {{Yes}}
| [[UTF-8]]
| {{Yes}}
| [[Python (programming language)|Python]]
| [[Zope Object Database|ZODB - Zope Object Database]]
|-class="sortbottom"
! style="width: 12em" |
! Creator
! First public release date
! Latest stable release
! Stable release date
! Predecessor
! Software license<ref name="license"/>
! Open source
! Encoding
! Multilingual
! Programming language
! [[Data management|Data backend]]
|}
"""

  "Sweble 2 parser" should "parse simple wikitext" in {
    val parser = new Sweble2Parser
    parser.parse(code, title)
  }


}
