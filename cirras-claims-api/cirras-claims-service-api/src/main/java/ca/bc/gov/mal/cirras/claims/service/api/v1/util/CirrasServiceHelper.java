package ca.bc.gov.mal.cirras.claims.service.api.v1.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantityDetail;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationBerriesDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainQuantityDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainQuantityDetailDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainSpotLossDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainUnseededDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrapesDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationPlantAcresDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationPlantUnitsDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationUserDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationVarietyDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDetailDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationUserDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.IntegrityConstraintViolatedDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.OptimisticLockingFailureDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class CirrasServiceHelper {

	private static final Logger logger = LoggerFactory.getLogger(CirrasServiceHelper.class);

	// daos
	private ClaimCalculationDao claimCalculationDao;
	private ClaimCalculationVarietyDao claimCalculationVarietyDao;
	private ClaimCalculationBerriesDao claimCalculationBerriesDao;
	private ClaimCalculationPlantUnitsDao claimCalculationPlantUnitsDao;
	private ClaimCalculationPlantAcresDao claimCalculationPlantAcresDao;
	private ClaimCalculationGrapesDao claimCalculationGrapesDao;
	private ClaimCalculationGrainUnseededDao claimCalculationGrainUnseededDao;
	private ClaimCalculationGrainSpotLossDao claimCalculationGrainSpotLossDao;
	private ClaimCalculationGrainQuantityDao claimCalculationGrainQuantityDao;
	private ClaimCalculationGrainQuantityDetailDao claimCalculationGrainQuantityDetailDao;
	private ClaimCalculationUserDao claimCalculationUserDao;

	public void setClaimCalculationDao(ClaimCalculationDao claimCalculationDao) {
		this.claimCalculationDao = claimCalculationDao;
	}

	public void setClaimCalculationVarietyDao(ClaimCalculationVarietyDao claimCalculationVarietyDao) {
		this.claimCalculationVarietyDao = claimCalculationVarietyDao;
	}

	public void setClaimCalculationBerriesDao(ClaimCalculationBerriesDao claimCalculationBerriesDao) {
		this.claimCalculationBerriesDao = claimCalculationBerriesDao;
	}

	public void setClaimCalculationGrapesDao(ClaimCalculationGrapesDao claimCalculationGrapesDao) {
		this.claimCalculationGrapesDao = claimCalculationGrapesDao;
	}
	
	public void setClaimCalculationGrainUnseededDao(ClaimCalculationGrainUnseededDao claimCalculationGrainUnseededDao) {
		this.claimCalculationGrainUnseededDao = claimCalculationGrainUnseededDao;
	}

	public void setClaimCalculationGrainSpotLossDao(ClaimCalculationGrainSpotLossDao claimCalculationGrainSpotLossDao) {
		this.claimCalculationGrainSpotLossDao = claimCalculationGrainSpotLossDao;
	}

	public void setClaimCalculationGrainQuantityDao(ClaimCalculationGrainQuantityDao claimCalculationGrainQuantityDao) {
		this.claimCalculationGrainQuantityDao = claimCalculationGrainQuantityDao;
	}

	public void setClaimCalculationGrainQuantityDetailDao(ClaimCalculationGrainQuantityDetailDao claimCalculationGrainQuantityDetailDao) {
		this.claimCalculationGrainQuantityDetailDao = claimCalculationGrainQuantityDetailDao;
	}
	
	public void setClaimCalculationPlantUnitsDao(ClaimCalculationPlantUnitsDao claimCalculationPlantUnitsDao) {
		this.claimCalculationPlantUnitsDao = claimCalculationPlantUnitsDao;
	}

	public void setClaimCalculationPlantAcresDao(ClaimCalculationPlantAcresDao claimCalculationPlantAcresDao) {
		this.claimCalculationPlantAcresDao = claimCalculationPlantAcresDao;
	}
	
	public void setClaimCalculationUserDao(ClaimCalculationUserDao claimCalculationUserDao) {
		this.claimCalculationUserDao = claimCalculationUserDao;
	}
	
	// Retrieves the ClaimCalculationUserDto corresponding to the given authentication.
	// If it does not exist, it is created. If Given Name or Family Name has changed, it is updated.
	// If it cannot be determined, then returns null.
	public ClaimCalculationUserDto getClaimCalculationUserDto(WebAdeAuthentication authentication) throws ServiceException, NotFoundException {
		logger.debug("<getClaimCalculationUserDto");
			
		ClaimCalculationUserDto dto = null;

		if (authentication != null && authentication.getUserGuid() != null) {

			try {
				String claimCalcUserGuid = null;

				dto = claimCalculationUserDao.getByLoginUserGuid(authentication.getUserGuid());
				
				//Service accounts don't have a family name set. To prevent showing unknown in the app the
				//family name is set to System
				String familyName = null;
				if(authentication.getFamilyName() == null && authentication.getUserTypeCode().equalsIgnoreCase("SCL")) {
					familyName = "System";
				} else {
					familyName = authentication.getFamilyName();
				}
				

				if (dto == null) {
					dto = new ClaimCalculationUserDto();

					dto.setClaimCalculationUserGuid(null);
					dto.setFamilyName(familyName);
					dto.setGivenName(authentication.getGivenName());
					dto.setLoginUserGuid(authentication.getUserGuid());
					dto.setLoginUserId(authentication.getUserId());
					dto.setLoginUserType(authentication.getUserTypeCode());

					claimCalculationUserDao.insert(dto, dto.getLoginUserId());

					claimCalcUserGuid = dto.getClaimCalculationUserGuid();						
					dto = claimCalculationUserDao.fetch(claimCalcUserGuid);

					if (dto == null) {
						throw new NotFoundException("Did not find the claim calc user: " + claimCalcUserGuid);
					}				
				
				} else {
												
					// Update Given Name and Family Name in case they changed.
					dto.setFamilyName(familyName);
					dto.setGivenName(authentication.getGivenName());
						
					if (dto.isDirty()) {
						claimCalcUserGuid = dto.getClaimCalculationUserGuid();
						claimCalculationUserDao.update(dto, dto.getLoginUserId());
						dto = claimCalculationUserDao.fetch(claimCalcUserGuid);

						if (dto == null) {
							throw new NotFoundException("Did not find the claim calc user: " + claimCalcUserGuid);
						}
					
					}
				}

			} catch (DaoException e) {
				throw new ServiceException("DAO threw an exception", e);
			}
		}

		logger.debug(">getClaimCalculationUserDto");
		return dto;
	}
	
	public void deleteClaimCalculation(String claimCalculationGuid, Boolean doDeleteLinkedCalculations)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException {
		logger.debug("<deleteClaimCalculation");

		try {
			ClaimCalculationDto dto = claimCalculationDao.fetch(claimCalculationGuid);

			if (dto == null) {
				throw new NotFoundException("Did not find the Group: " + claimCalculationGuid);
			}

			//Get shared grain quantity record before the claim calculation is deleted
			ClaimCalculationGrainQuantityDto grainQtyDto = claimCalculationGrainQuantityDao.select(claimCalculationGuid);
			
			// Delete subtables
			claimCalculationVarietyDao.deleteForClaim(claimCalculationGuid);
			claimCalculationGrapesDao.deleteForClaim(claimCalculationGuid);
			claimCalculationBerriesDao.deleteForClaim(claimCalculationGuid);
			claimCalculationPlantUnitsDao.deleteForClaim(claimCalculationGuid);
			claimCalculationPlantAcresDao.deleteForClaim(claimCalculationGuid);
			claimCalculationGrainUnseededDao.deleteForClaim(claimCalculationGuid);
			claimCalculationGrainSpotLossDao.deleteForClaim(claimCalculationGuid);
			claimCalculationGrainQuantityDetailDao.deleteForClaim(claimCalculationGuid);
			claimCalculationDao.delete(claimCalculationGuid);
			
			//Grain Quantity sub table might be used by another calculation
			//It's only deleted if there is no other calculation associated with it
			// or if doDeleteLinkedCalculations is true
			if(grainQtyDto != null) {
				//Check for other calculations associated with it
				List<ClaimCalculationDto> calculationsDto = claimCalculationDao.getCalculationsByGrainQuantityGuid(grainQtyDto.getClaimCalculationGrainQuantityGuid());
				if(calculationsDto != null && calculationsDto.size() > 0) {
					//Only delete associated calculation and grain quantity record if the flag is true  
					if(doDeleteLinkedCalculations) {
						for (ClaimCalculationDto claimCalculationDto : calculationsDto) {
							claimCalculationDao.delete(claimCalculationDto.getClaimCalculationGuid());
						}
						claimCalculationGrainQuantityDao.delete(grainQtyDto.getClaimCalculationGrainQuantityGuid());
					} 
				} else {
					claimCalculationGrainQuantityDao.delete(grainQtyDto.getClaimCalculationGrainQuantityGuid());
				}
			}

		} catch (IntegrityConstraintViolatedDaoException e) {
			throw new ConflictException(e.getMessage());
		} catch (OptimisticLockingFailureDaoException e) {
			throw new ConflictException(e.getMessage());
		} catch (NotFoundDaoException e) {
			throw new NotFoundException(e.getMessage());
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">deleteClaimCalculation");
	}
	
	//Compares two date values and returns true if both are the same or both are NULL
	//It only compares the date and IGNORES the time
	//Reason: Oracle stores dates that are compared here with time whereas in postgres it's stored without time
	public boolean equals(String propertyName, Date v1, Date v2) {
		boolean result = false;
		
		if(v1==null&&v2==null) {
			
			result = true;
		} else if(v1!=null&&v2!=null) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			result = sdf.format(v1).equals(sdf.format(v2));

		} else {
			
			result = false;
		}
		
		if(!result&&logger!=null) {
			logger.info(propertyName+" is dirty. old:"+v2+" new:"+v1);
		}
		
		return result;
	}
	
	public String capitalizeEachWord(String str){
	    
	    //if string is null or empty, return empty string
	    if(str == null || str.length() == 0)
	        return "";
	    
	    /* 
	     * if string contains only one char,
	     * make it capital and return 
	     */
	    if(str.length() == 1)
	        return str.toUpperCase();
	    
	    
	    /*
	     * Split the string by word boundaries
	     */
	    String[] words = str.split("\\W");
	    
	    //create empty StringBuilder with same length as string
	    StringBuilder sbCapitalizedWords = new StringBuilder(str.length());
	    
	    for(String word : words){            
	        
	        if(word.length() > 1)
	            sbCapitalizedWords
	                .append(word.substring(0, 1).toUpperCase())
	                .append(word.substring(1).toLowerCase());
	        else
	            sbCapitalizedWords.append(word.toUpperCase());
	        
	        //we do not want to go beyond string length
	        if(sbCapitalizedWords.length() < str.length()){
	            sbCapitalizedWords.append(str.charAt(sbCapitalizedWords.length()));
	        }
	    }
	    
	    /*
	     * convert StringBuilder to string, also
	     * remove last space from it using trim method, if there is any
	     */
	    return sbCapitalizedWords.toString().trim();
	}
	
	public Integer toInteger(String value) {
		Integer result = null;
		if(value!=null&&value.trim().length()>0) {
			result = Integer.valueOf(value);
		}
		return result;
	}
}
