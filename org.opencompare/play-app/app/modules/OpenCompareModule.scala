package modules

import com.google.inject.{Provides, AbstractModule}
import models.PCMAPIUtils
import models.daos.{UserDAO, PCMContainerDAO, PCMContainerDAOImpl}
import net.codingwell.scalaguice.ScalaModule

/**
 * Created by gbecan on 10/12/15.
 */
class OpenCompareModule extends AbstractModule with ScalaModule {


  override def configure() {
    bind[PCMContainerDAO].to[PCMContainerDAOImpl]
  }


  @Provides
  def providePCMAPIUtils(userDAO : UserDAO) : PCMAPIUtils = new PCMAPIUtils(userDAO)


}
