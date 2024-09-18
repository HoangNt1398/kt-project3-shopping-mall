import slideImg1 from "../../assets/slide/slideImg1.jpg";
import slideImg2 from "../../assets/slide/slideImg2.jpg";
import slideImg3 from "../../assets/slide/slideImg3.jpg";
import styled from "styled-components";

import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

function Slide() {
  const settings = {
    dots: true,
    infinite: true,
    speed: 300,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
  };

  return (
    <Wrapper>
      <Slider {...settings}>
        <div>
          <img src={slideImg1} alt="sale" />
        </div>
        <div>
          <img src={slideImg2} alt="sale" />
        </div>
        <div>
          <img src={slideImg3} alt="sale" />
        </div>
      </Slider>
    </Wrapper>
  );
}

export default Slide;

const Wrapper = styled.div`
  margin-top: 3rem;
  z-index: 500;

  img {
    width: 100%;
  }
`;
