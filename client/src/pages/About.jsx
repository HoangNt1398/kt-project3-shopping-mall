import React from "react";
import styled from "styled-components";
import aboutImg from "../assets/img/aboutImge.jpeg";
import Fade from "react-reveal/Fade";

export default function About() {
  return (
    <Wrapper>
      {/* <Fade left> */}
      <LeftWrapper>
        <Title>Soleilique</Title>
        <p>Soleilique에 오신 것을 환영합니다!</p>
        <p>Soleilique는 다양한 상점의 제품을 한곳에 모은 신뢰할 수 있는 온라인 쇼핑몰입니다.</p>
        <p>여기서 여러분은 다양한 상점에서 수천 가지 상품을 탐색하고 쉽게 선택할 수 있습니다.</p>
        <p>패션, 액세서리, 화장품부터 가정용품과 전자기기까지, 필요한 모든 것을 Soleilique에서 만나보세요.</p>
        <p>저희는 품질과 서비스를 최우선으로 생각합니다.</p>
        <p>신뢰할 수 있는 상점들과 직접 연결하여 정품을 합리적인 가격에 제공합니다.</p>
        <p> 또한 다양한 혜택과 할인도 준비되어 있습니다.</p>
        <p>지금 바로 Soleilique에서 쇼핑을 시작해 보세요.</p>
        <p>최고의 상점들이 한자리에 모여 여러분을 기다리고 있습니다!</p>
      </LeftWrapper>
      {/* </Fade> */}

      <RightWrapper>
        <Fade right>
          <img src={aboutImg} alt="dogCatImg" />
        </Fade>
      </RightWrapper>
    </Wrapper>
  );
}

const Wrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 6rem 2rem 0 1rem;
  font-family: "Dovemayo_gothic";

  @media screen and (max-width: 1000px) {
    flex-direction: column;
  }
`;
const LeftWrapper = styled.div`
  flex: 1;
  /* margin-right: 3rem; */

  p {
    margin: 1.2rem 0 1.2rem 1.2rem;
    font-size: 1.1rem;
  }
`;

const Title = styled.div`
  font-size: 2rem;
  margin: 2rem 0 2rem 1rem;
`;

const RightWrapper = styled.div`
  flex: 1;
  img {
    width: 500px;
    height: 500px;
    border-radius: 15px;
    margin-left: 2rem;

    @media screen and (max-width: 1024px) {
      width: 430px;
      height: 430px;
      margin-top: 2rem;
    }

    @media screen and (max-width: 768px) {
      width: 300px;
      height: 300px;
      margin-top: 2rem;
    }
  }
`;