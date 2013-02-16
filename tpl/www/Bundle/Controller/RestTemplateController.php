<#assign curr = entities[current_entity] />
<?php

namespace ${project_name?cap_first}\ApiBundle\Controller;

use FOS\RestBundle\Controller\FOSRestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use ${project_name?cap_first}\ApiBundle\Entity\${curr.name?cap_first};
<#assign importList = [] />
<#list curr.relations as relation>
	<#if !importList?seq_contains(relation.relation.targetEntity)>
use ${project_name?cap_first}\ApiBundle\Controller\Rest${relation.relation.targetEntity?cap_first}Controller;
		<#assign importList = importList + [relation.relation.targetEntity] />
	</#if>
</#list>

/**
 * PostRepository
 *
 * This class was generated by the Doctrine ORM. Add your own custom
 * repository methods below.
 */
class Rest${curr.name?cap_first}Controller extends FOSRestController{
<#list curr.fields as field>
	<#if !field.internal>
	const ${field.name?upper_case} = "${field.name?uncap_first}";
	</#if>
</#list>
	const SYNCUDATE = "sync_uDate";
	const SYNCDTAG = "sync_dtag";
	const ID_MOBILE = "mobile_id";


	/**=========================================================================
	 * CRUD REST
	 *========================================================================*/


	/** 
	* GET /${curr.options.rest.uri}/{id} 
	*  
	* @param string $${curr.name?uncap_first}Id
	* @return Response 
	*
	* @Rest\Get("/${curr.options.rest.uri}/{id}")
	*/  
	public function get${curr.name?cap_first}Action($id){  
		$em = $this->getDoctrine()->getManager();
		$data = $em->getRepository('${project_name?cap_first}ApiBundle:${curr.name?cap_first}')->find($id);
		$view = $this->view(array("${curr.name?cap_first}" => self::toJson($data)), 200);
		return $this->handleView($view);
	}	

	/** 
	* PUT /${curr.options.rest.uri} 
	*
	* @Rest\Put("/${curr.options.rest.uri}")
	*/  
	public function create${curr.name?cap_first}Action(){  
		$${curr.name?uncap_first} = new ${curr.name?cap_first}();
		$json = json_decode($this->getRequest()->getContent(), true);
		$this->extract($${curr.name?uncap_first}, $json['${curr.name?cap_first}']);
		$em = $this->getDoctrine()->getManager();
		$em->persist($${curr.name?uncap_first});
		$em->flush();
		$view = $this->view(array("Ok" => self::toJson($${curr.name?uncap_first})), 200);
		return $this->handleView($view);
	}	


	/** 
	* GET /${curr.options.rest.uri}
	*
	* @Rest\Get("/${curr.options.rest.uri}")
	*/  
	public function getAll${curr.name?cap_first}Action(){  
		$em = $this->getDoctrine()->getManager();
		$data = $em->getRepository('${project_name?cap_first}ApiBundle:${curr.name?cap_first}')->findAll();
		$view = $this->view(array("${curr.name?cap_first}s" => self::arrayToJson($data)), 200);
		return $this->handleView($view);
	}	

	/** 
	* POST /${curr.options.rest.uri}/{id} 
	*
	* @Rest\Post("/${curr.options.rest.uri}/{id}")
	*/  
	public function update${curr.name?cap_first}Action($id){  
		$em = $this->getDoctrine()->getManager();
		$${curr.name?uncap_first} = $em->getRepository('${project_name?cap_first}ApiBundle:${curr.name?cap_first}')->find($id);
		$json = json_decode($this->getRequest()->getContent(), true);
		$this->extract($${curr.name?uncap_first}, $json['${curr.name?cap_first}']);
		$em->persist($${curr.name?uncap_first});
		$em->flush();
		$view = $this->view(array("Ok" => self::toJson($${curr.name?uncap_first})), 200);
		return $this->handleView($view);
	}	


	/** 
	* DELETE /${curr.options.rest.uri}/{id} 
	*
	* @Rest\Delete("/${curr.options.rest.uri}/{id}")
	*/  
	public function delete${curr.name?cap_first}Action($id){  
		$em = $this->getDoctrine()->getManager();
		$entity = $em->getRepository('${project_name?cap_first}ApiBundle:${curr.name?cap_first}')->find($id);

		if (!$entity) {
			throw $this->createNotFoundException('Unable to find ${curr.name?cap_first} entity.');
		}

		$em->remove($entity);
		$em->flush();

		$view = $this->view($entity, 200);
		return $this->handleView($view);

	}	

	
	/**=========================================================================
	 * Sync Engine
	 *========================================================================*/
	private $${curr.name?uncap_first}sm;
	private $${curr.name?uncap_first}si;
	private $${curr.name?uncap_first}su;
	private $${curr.name?uncap_first}sd;
	private $logger;
	private $client;
	
	
	/**
	 * POST
	 *
	 * @Rest\Post("/${curr.options.rest.uri}")
	 */
	public function sync${curr.name?cap_first}Action() {
		// Debug
		$fichier = fopen('insert_json.txt','w+');
		fputs($fichier,$this->getRequest()->getContent());
		fclose($fichier);
		
		// Call
		$this->${curr.name?uncap_first}sm = array();
		$this->${curr.name?uncap_first}si = array();
		$this->${curr.name?uncap_first}su = array();
		$this->${curr.name?uncap_first}sd = array();
		$this->logger = $this->get('logger');
		$this->client = $this->container->get('request')->getClientIp();
		
		$json = json_decode($this->getRequest()->getContent(), true);
		
		$lastSyncDate = new \DateTime($json['lastSyncDate']); 
		$startSyncDate = new \DateTime($json['startSyncDate']);
		
 		$this->logger->debug('Start Sync '. $this->client . ' @:' . $startSyncDate->format('H:i:s.u') . ' last:' . $lastSyncDate->format('H:i:s.u')); // \DateTime::ISO860
		
		$em = $this->getDoctrine()->getManager();
		$this->delete($em, $json, $startSyncDate);
		$this->insert($em, $json, $startSyncDate);
		$this->update($em, $json, $startSyncDate);
		$this->merge ($em, $lastSyncDate);
		
		// Final
		$em->flush();
		
		$view = $this->view(array(
			'${curr.options.rest.uri}s-m' => self::arrayToJson($this->${curr.name?uncap_first}sm),
			'${curr.options.rest.uri}s-i' => self::arrayToJson($this->${curr.name?uncap_first}si),
			'${curr.options.rest.uri}s-u' => self::arrayToJson($this->${curr.name?uncap_first}su),
			'${curr.options.rest.uri}s-d' => self::arrayToJson($this->${curr.name?uncap_first}sd)
			), 200);
		return $this->handleView($view);
	}
	
	/** 
	* GET /${curr.options.rest.uri}/merge
	*/
	private function merge($em, $lastSyncDate){
		$repository = $this->getDoctrine()
			->getRepository('DemactApiBundle:${curr.name?cap_first}');

		$query = $repository->createQueryBuilder('u')
			->where('u.sync_uDate >= :date')
			->setParameter('date', $lastSyncDate)
			->getQuery();

		$this->${curr.name?uncap_first}sm = $query->getResult();
		
		
 		$this->logger->debug("\tServer sync Client:" . $this->client . " append " . count($this->${curr.name?uncap_first}sm)); // . $entityBase->toString());
	}
	
	
	/** 
	* PUT /${curr.options.rest.uri}/insert
	*/
	private function insert($em, $json, $startSyncDate){
		foreach($json['${curr.options.rest.uri}s-i'] as $json_${curr.name?uncap_first}){
			$entity = new ${curr.name?cap_first}();
			$this->extract($entity, $json_${curr.name?uncap_first});
			
			// Insert to server with new entity (db inserted)
			$entity->setSyncUDate($startSyncDate);
			
			// Sync data
			$em->persist($entity);
			
			$this->logger->debug('\tServer sync Client:' . $this->client . ' inserted ' . $entity->toString());
			$this->${curr.name?uncap_first}si[] = $entity;
		}
	}
	
	/** 
	* POST /${curr.options.rest.uri}/update
	*/
	private function update($em, $json, $startSyncDate){
		foreach($json['${curr.options.rest.uri}s-u'] as $json_${curr.name?uncap_first}){
			$entity = new ${curr.name?cap_first}();
			$this->extract($entity, $json_${curr.name?uncap_first});
			
			// check if sync
			if ($entity->getId() != null) {
				$selectEntity = $em->getRepository('DemactApiBundle:${curr.name?cap_first}')->find($entity->getId());
					
				if ($entity->getSyncUDate() > $selectEntity->getSyncUDate()) {
					// Update server with updated entity (db updated)
					$entity->setSyncUDate($startSyncDate);
					$selectEntity->setSyncUDate($startSyncDate);
						
					// Sync data
					$em->merge($entity);
					
 					$this->logger->debug("\tServer sync Client:" . $this->client . " updated " . $entity->toString());
				} else {
					// Update mobile with updated entity (db updated)
					$entity->setSyncDTag(false);
					$entity->setSyncUDate($startSyncDate);
						
					// Sync data
 					$this->logger->debug("\tServer sync Client:" . $this->client . " refresh " . $entity->toString());
				}
				
				$this->${curr.name?uncap_first}su[] = $entity;
			} else {
				$this->logger->debug("\tServer sync Client:" . $this->client . " WHY NOT INSERT !!!!!!!!! " . $entity->toString());
			}
		}
	}
	
	/** 
	* DELETE /${curr.options.rest.uri}/delete 
	*/
	private function delete($em, $json, $startSyncDate){
		foreach($json['${curr.options.rest.uri}s-d'] as $json_${curr.name?uncap_first}){
			$entity = new ${curr.name?cap_first}();
			$this->extract($entity, $json_${curr.name?uncap_first});
			
			// check if sync
			if ($entity->getId() != null) {
				$this->logger->debug("\tServer sync Client: find " .$entity->getId());
				$selectEntity = $em->getRepository('DemactApiBundle:${curr.name?cap_first}')->find($entity->getId());
				
				if ($entity->getSyncUDate() > $selectEntity->getSyncUDate()) {
					// Update/Delete server with updated entity (db updated)
					$selectEntity->setSyncUDate($startSyncDate);
					$selectEntity->setSyncDtag($entity->getSyncDtag());
					$entity->setSyncUDate($startSyncDate);
					
					// Sync data
					$em->merge($entity);
					
 					$this->logger->debug("\tServer sync Client:" . $this->client . " delete " . $entity->toString());
				} else {
					// Update mobile with updated entity (just db select)
					$entity->setSyncDtag(false);
					$entity->setSyncUDate($selectEntity->getSyncUDate());
						
					// Sync data
 					$this->logger->debug("\tServer sync Client:" . $this->client . " re-enable and updated " . $entity->toString());
				}
				
				$this->${curr.name?uncap_first}sd[] = $entity;
			} else {
				// Nothing to do.
 				$this->logger->debug("\tServer sync Client:" . $this->client . " nothing to delete " . $entity->toString());
			}
		}
		
	}


	
	/**=========================================================================
	 * Converter Json
	 *========================================================================*/

	/**
	 * Convert a ${curr.name?uncap_first} to a JSon
	 *
	 */
	public static function toJson($${curr.name?uncap_first}){
		if($${curr.name?uncap_first}!=null){
			$json = array();
<#list curr.fields as field>
	<#if !field.internal>
		<#if !field.relation??>
			<#if field.type=="date" || field.type=="datetime" || field.type=="time">
			$json[self::${field.name?upper_case}] = $${curr.name?uncap_first}->get${field.name?cap_first}()->format(\DateTime::W3C);
			<#else>
			$json[self::${field.name?upper_case}] = $${curr.name?uncap_first}->get${field.name?cap_first}();
			</#if>
		<#else>
			<#if field.relation.type=="OneToOne" || field.relation.type=="ManyToOne">
			$json[self::${field.name?upper_case}] = Rest${field.relation.targetEntity?cap_first}Controller::idToJson($${curr.name?uncap_first}->get${field.name?cap_first}());
			<#else>
			$json[self::${field.name?upper_case}] = Rest${field.relation.targetEntity?cap_first}Controller::arrayToJson($${curr.name?uncap_first}->get${field.name?cap_first}());
			</#if>
		</#if>
	</#if>
</#list>
			$json[self::ID_MOBILE] = $${curr.name?uncap_first}->getMobileId();
			$json[self::SYNCUDATE] = $${curr.name?uncap_first}->getSyncUDate()->format(\DateTime::W3C);
			$json[self::SYNCDTAG] = $${curr.name?uncap_first}->getSyncDtag();

			return $json;
		}else{
			return null;
		}
	}


	/**
	 * Convert a comment to a JSon
	 *
	 */
	public static function idToJson($${curr.name?uncap_first}){
		if($${curr.name?uncap_first}!=null){
			$json = array();
			$json[self::ID] = $${curr.name?uncap_first}->getId();
			return $json;
		}else{
			return null;
		}
	}


	/**
	 * Convert an array of ${curr.name?uncap_first}s to a JSon
	 *
	 */
	public static function arrayToJson($${curr.name?uncap_first}s){
		$json = array();
		foreach($${curr.name?uncap_first}s as $${curr.name?uncap_first}){
			$json[] = self::toJson($${curr.name?uncap_first});
		}
		return $json;
	}

	/**
	 * Convert an JSONarray to an array of ${curr.name?uncap_first}s
	 *
	 */
	 public function extract${curr.name?cap_first}s($json){
		$${curr.name?uncap_first}s = array();
		$json_array = $json['${curr.name?cap_first}s'];
		foreach($json_array as $json_${curr.name?uncap_first}){
			$${curr.name?uncap_first} = new ${curr.name?cap_first}();
			$this->extract($${curr.name?uncap_first}, $json_${curr.name?uncap_first});
			$${curr.name?uncap_first}s[] = $${curr.name?uncap_first};
		}
		
		return $${curr.name?uncap_first}s;
	 }


	/**
	 * Convert a JSon to a ${curr.name?uncap_first}
	 *
	 */
	public function extract($${curr.name?uncap_first}, $json){
		if(array_key_exists(self::ID,$json)){
			$${curr.name?uncap_first}->setId($json[self::ID]);
		}
		if(array_key_exists(self::ID_MOBILE,$json)){
			$${curr.name?uncap_first}->setMobileId($json[self::ID_MOBILE]);
			//$this->logger->debug("extract obj=".$${curr.name?uncap_first}->getMobileId()."json=" . $json[self::ID_MOBILE] . ' local=' . $${curr.name?uncap_first}->getId() . ' localJson='); // . $json[self::ID]);
		}
<#list curr.fields as field>
	<#if !field.internal && !field.id>
		if(array_key_exists(self::${field.name?upper_case},$json)){
		<#if !field.relation??>
			<#if field.type=="date" || field.type=="time" || field.type=="datetime">
			$${curr.name?uncap_first}->set${field.name?cap_first}(new \DateTime($json[self::${field.name?upper_case}]));
			<#else>
			$${curr.name?uncap_first}->set${field.name?cap_first}($json[self::${field.name?upper_case}]);
			</#if>
		<#else>
			<#if field.relation.type=="OneToOne" || field.relation.type=="ManyToOne">
			$em = $this->getDoctrine()->getManager();
			$${field.name?uncap_first} = $em->getRepository('${project_name?cap_first}ApiBundle:${field.relation.targetEntity?cap_first}')->find($json[self::${field.name?upper_case}]['id']);
			$${curr.name?uncap_first}->set${field.name?cap_first}($${field.name?uncap_first});
			<#else>

			</#if>
		</#if>
		}
	</#if>
</#list>
		if(array_key_exists(self::SYNCUDATE,$json)){
			$${curr.name?uncap_first}->setSyncUDate(new \DateTime($json[self::SYNCUDATE]));
		}
		if(array_key_exists(self::SYNCDTAG,$json)){
			$${curr.name?uncap_first}->setSyncDtag($json[self::SYNCDTAG]);
		}
	}
}
