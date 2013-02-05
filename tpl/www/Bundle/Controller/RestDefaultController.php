<?php

namespace ${project_name?cap_first}\ApiBundle\Controller;

use FOS\RestBundle\Controller\FOSRestController;
use FOS\RestBundle\Controller\Annotations as Rest;

/**
 * DefaultController
 *
 * This class was generated by the Doctrine ORM. Add your own custom
 * controller methods below.
 */
class RestDefaultController extends FOSRestController{

	/**
	 * GET
	 *
	 * @Rest\Get("/sync")
	 */
	public function timeSyncAction() {
		$view = $this->view((new \DateTime())->format(\DateTime::W3C), 200);
		return $this->handleView($view);
	}
}

?>
