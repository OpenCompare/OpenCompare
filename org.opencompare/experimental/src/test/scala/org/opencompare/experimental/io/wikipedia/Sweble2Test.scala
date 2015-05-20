package org.opencompare.experimental.io.wikipedia

import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by gbecan on 5/20/15.
 */
class Sweble2Test extends FlatSpec with Matchers {


  "Sweble 2 parser" should "parse simple wikitext" in {
    val code = "{{other uses|Vila Verde (disambiguation)}}\n{{Infobox Municipality PT\n| image_flag      = Pt-vvd1.png\n| image_shield    = VVD.png\n| image_skyline   = \n| image_caption   = \n| latd  = 41 |latm  = 39 |latNS  = N\n| longd = 8 |longm = 26|longEW = W \n| image_map       = LocalVilaVerde.svg\n|region             = [[Norte Region, Portugal|Norte]]\n|subregion          = [[Cávado Subregion|Cávado]]\n|CIM                = [[Cávado (intermunicipal community)|Cávado]]\n|district           = [[Braga District|Braga]]\n| leader_party            = [[Social Democratic Party (Portugal)|PSD]]\n| leader_name             = [[António Vilela]]\n| area_total_km2          = 228.67\n| population_total        = 47,888\n| population_as_of        = 2011\n| parishes         = [[#Parishes|33]]\n| website                 = http://www.cm-vilaverde.pt\n}}\n'''Vila Verde''' ({{IPA-pt|ˈvilɐ ˈveɾð(ɨ)}}) is a [[List of municipalities of Portugal|municipality]] in the district of [[Braga (district)|Braga]] in [[Portugal]]. The population in 2011 was 47,888,<ref name=ine>[http://www.ine.pt/xportal/xmain?xlang=en&xpid=INE&xpgid=ine_indicadores&indOcorrCod=0005889&contexto=pi&selTab=tab0 Instituto Nacional de Estatística]</ref> in an area of 228.67&nbsp;km².<ref name=dgt>[http://www.dgterritorio.pt/ficheiros/cadastro/caop/caop_download/caop_2014_0/areasfregmundistcaop2014_2 Direção-Geral do Território]</ref>\n\nThe present Mayor is António Vilela, elected by the [[Social Democratic Party (Portugal)|Social Democratic Party]]. The municipal holiday is June 13.\n\n==Parishes==\n\nAdministratively, the municipality is divided into 33 civil parishes (''[[freguesia (Portugal)|freguesias]]''):<ref>{{cite web|title=Law nr. 11-A/2013, pages 552 138-139|url=http://dre.pt/pdf1sdip/2013/01/01901/0000200147.pdf |accessdate=5 August 2014|author=''[[Diário da República]]''|language=Portuguese|format=pdf}}</ref>\n{{div col|3}}\n* Aboim da Nóbrega e Gondomar\n* Atiães\n* Cabanelas\n* Carreiras (São Miguel e Santiago)\n* Cervães\n* Coucieiro\n* Dossãos\n* Escariz (São Mamede e São Martinho)\n* Esqueiros, Nevogilde e Travassós\n* Freiriz\n* Gême\n* Lage\n* Lanhas\n* Loureira\n* Marrancos e Arcozelo\n* Moure\n* Oleiros\n* Oriz (Santa Marinha e São Miguel)\n* Parada de Gatim\n* Pico\n* Pico de Regalados, Gondiães e Mós\n* Ponte\n* [[Ribeira do Neiva]]\n* Sabariz\n* Sande, Vilarinho, Barros e Gomide\n* São Miguel do Prado\n* Soutelo\n* Turiz\n* Vade\n* Valbom (São Pedro), Passô e Valbom (São Martinho) \n* Valdreu\n* Vila de Prado\n* Vila Verde e Barbudo\n{{div col end}}\n\n==Main sights==\nVila Verde and surrounding region have many historical monuments.\n\n*[[Penegate Tower]] \n\n==Gallery==\n<gallery>\nFile:Igreja de Carreiras S. Miguel.JPG|Saint Michael's church, Carreiras\nFile:Torre de Penegate.JPG|Penegate tower\n</gallery>\n== References==\n{{reflist}}\n\n==External links==\n*[http://www.cm-vilaverde.pt Municipality official website]\n\n{{commons cat|Vila Verde}}\n\n{{Municipalities of Braga}}\n\n[[Category:Towns in Portugal]]\n[[Category:Municipalities of Braga District]]\n[[Category:Vila Verde| ]]\n\n{{Braga-geo-stub}}"
    val title = "Vila Verde"
    val parser = new Sweble2Parser
    parser.parse(code, title)
  }


}
