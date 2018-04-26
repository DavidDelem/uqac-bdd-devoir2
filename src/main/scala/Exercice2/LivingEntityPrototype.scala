package Exercice2

import Exercice2.Bestiary._

object LivingEntityPrototype {

  def create(livingEntity: LivingEntity): LivingEntity ={

    var newLivingEntity: LivingEntity = null

    livingEntity match {
      case anglerSlayer:AngelSlayer => newLivingEntity = new AngelSlayer(anglerSlayer.position, anglerSlayer.id)
      case astralDeva:AstralDeva => newLivingEntity = new AstralDeva(astralDeva.position, astralDeva.id)
      case brutalWarlord:BrutalWarlord => newLivingEntity = new BrutalWarlord(brutalWarlord.position, brutalWarlord.id)
      case doubleAxeFury:DoubleAxeFury => newLivingEntity = new DoubleAxeFury(doubleAxeFury.position, doubleAxeFury.id)
      case greataxeOrc:GreataxeOrc => newLivingEntity = new GreataxeOrc(greataxeOrc.position, greataxeOrc.id)
      case greenGreatWyrmDragon: GreenGreatWyrmDragon => newLivingEntity = new GreenGreatWyrmDragon(greenGreatWyrmDragon.position, greenGreatWyrmDragon.id)
      case movanicDeva: MovanicDeva => newLivingEntity = new MovanicDeva(movanicDeva.position, movanicDeva.id)
      case orcWorgRider: OrcWorgRider => newLivingEntity = new OrcWorgRider(orcWorgRider.position, orcWorgRider.id)
      case planetar: Planetar => newLivingEntity = new Planetar(planetar.position, planetar.id)
      case solar: Solar => newLivingEntity = new Solar(solar.position, solar.id)
    }

    newLivingEntity.position = livingEntity.position
    newLivingEntity.targets = livingEntity.targets
    newLivingEntity.hp = livingEntity.hp

    newLivingEntity
  }

}
