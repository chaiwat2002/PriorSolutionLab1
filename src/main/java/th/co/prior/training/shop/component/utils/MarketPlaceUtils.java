package th.co.prior.training.shop.component.utils;

import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.MarketPlaceModel;

import java.util.List;

public interface MarketPlaceUtils {

    List<MarketPlaceModel> toDTOList(List<MarketPlaceEntity> marketPlace);

    MarketPlaceModel toDTO(MarketPlaceEntity marketPlace);

}
