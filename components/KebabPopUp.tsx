import React from 'react';
import { BottomSheet, Icon, ListItem } from 'react-native-elements';
import { Theme } from '../components/ui/styleUtils';
import { Centered, Column, Row, Text } from '../components/ui';
import { WalletBinding } from '../screens/Home/MyVcs/WalletBinding';
import { Pressable } from 'react-native';
import { useKebabPopUp } from './KebabPopUpController';
import { ActorRefFrom } from 'xstate';
import { vcItemMachine } from '../machines/vcItem';
import { useTranslation } from 'react-i18next';
import { HistoryTab } from '../screens/Home/MyVcs/HistoryTab';
import { RemoveVcWarningOverlay } from '../screens/Home/MyVcs/RemoveVcWarningOverlay';

export const KebabPopUp: React.FC<KebabPopUpProps> = (props) => {
  const controller = useKebabPopUp(props);
  const { t } = useTranslation('HomeScreenKebabPopUp');
  return (
    <Column>
      <Icon
        name={props.iconName}
        type={props.iconType}
        color={Theme.Colors.GrayIcon}
      />
      <BottomSheet
        isVisible={props.isVisible}
        containerStyle={Theme.KebabPopUpStyles.kebabPopUp}>
        <Row style={Theme.KebabPopUpStyles.kebabHeaderStyle}>
          <Text weight="bold">{t('title')}</Text>
          <Icon
            name="close"
            onPress={props.onDismiss}
            color={Theme.Colors.Details}
            size={25}
          />
        </Row>
        <Column>
          <ListItem bottomDivider>
            <ListItem.Content>
              <ListItem.Title>
                <Pressable onPress={controller.PIN_CARD}>
                  <Text size="small" weight="bold">
                    {props.vcKey.split(':')[4] == 'true'
                      ? t('unPinCard')
                      : t('pinCard')}
                  </Text>
                </Pressable>
              </ListItem.Title>
            </ListItem.Content>
          </ListItem>

          <WalletBinding
            label={t('offlineAuthenticationDisabled!')}
            content={t('offlineAuthDisabledMessage')}
            service={props.service}
          />

          <HistoryTab
            service={props.service}
            label={t('viewActivityLog')}
            vcKey={props.vcKey}
          />

          <ListItem bottomDivider>
            <ListItem.Content>
              <ListItem.Title>
                <Pressable onPress={() => controller.REMOVE(props.vcKey)}>
                  <Text size="small" weight="bold">
                    {t('removeFromWallet')}
                  </Text>
                </Pressable>
              </ListItem.Title>
            </ListItem.Content>
          </ListItem>

          <RemoveVcWarningOverlay
            isVisible={controller.isRemoveWalletWarning}
            onConfirm={controller.CONFIRM}
            onCancel={controller.CANCEL}
          />
        </Column>
      </BottomSheet>
    </Column>
  );
};

export interface KebabPopUpProps {
  iconName: string;
  iconType?: string;
  vcKey: string;
  isVisible: boolean;
  onDismiss: () => void;
  service: ActorRefFrom<typeof vcItemMachine>;
}